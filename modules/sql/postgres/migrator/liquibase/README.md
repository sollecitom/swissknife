# SQL Postgres Migrator Liquibase

This module replaces Liquibase's questionable locking mechanism by an alternative that avoids having to manually unstuck the lock table (`databasechangeloglock`).

## Problem

Liquibase uses a row into the table `databasechangeloglock` to signal to that a migration is in progress, so that multiple instances of a service don't attempt to migrate in parallel.

At the beginning of a migration, Liquibase updates the `databasechangeloglock` table to mark the lock as locked. At the end of a successful migration, it updates this table to mark the lock as unlocked.

The problem is that, if the instance that's running the migration dies (or if the connection is severed) before the lock is marked as unlocked, then the table is left "locked", and any further attempt will not run the migrations.

Normally, Liquibase requires that somebody accesses the database and manually updates the `databasechangeloglock` table to unlock it. In production environments, this is not really feasible.

This problem is particularly annoying when the migration is performed from a Kubernetes pod, as Kubernetes can kill the containers at any point.

## Solution

Replace the default Liquibase lock service with one specific to Postgres, `PostgresAdvisoryLockService`, which serialises migrations with a session-level
[advisory lock](https://www.postgresql.org/docs/current/explicit-locking.html#ADVISORY-LOCKS) instead of the `databasechangeloglock` row.

A migrator waits until it can take the lock (up to Liquibase's lock wait time), migrates, and releases it. If the migrator dies or its connection is severed,
Postgres releases the lock when the session ends, so there's never a stale lock to clean up and no takeover logic that two instances could race on.

Advisory locks are scoped to the current database, so services sharing a Postgres server don't block each other.

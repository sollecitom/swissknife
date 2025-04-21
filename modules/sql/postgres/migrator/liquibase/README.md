# SQL Postgres Migrator Liquibase

This module replaces Liquibase's questionable locking mechanism by an alternative that avoids having to manually unstuck the lock table (`databasechangeloglock`).

## Problem

Liquibase uses a row into the table `databasechangeloglock` to signal to that a migration is in progress, so that multiple instances of a service don't attempt to migrate in parallel.

At the beginning of a migration, Liquibase updates the `databasechangeloglock` table to mark the lock as locked. At the end of a successful migration, it updates this table to mark the lock as unlocked.

The problem is that, if the instance that's running the migration dies (or if the connection is severed) before the lock is marked as unlocked, then the table is left "locked", and any further attempt will not run the migrations.

Normally, Liquibase requires that somebody accesses the database and manually updates the `databasechangeloglock` table to unlock it. In production environments, this is not really feasible.

This problem is particularly annoying when the migration is performed from a Kubernetes pod, as Kubernetes can kill the containers at any point.

## Solution

Replace the default Liquibase lock service implementation with another one, specific to Postgres, which uses unique database session IDs to check whether the session that locked the `databasechangeloglock` table is still active.

If it's still active, then the instance waits, as the migration is in progress. If the session that locked the `databasechangeloglock` table is not active anymore, but the lock is still on, it means it died, and it's safe to acquire the lock and continue the migration.

## Notes

- This solution is heavily inspired by https://github.com/oridool/liquibase-locking.
    - The decision to move this within swissknife was made because this project hadn't been maintained in a while, and the implementation was a bit messy.
package sollecitom.libs.swissknife.test.utils.params

import org.junit.jupiter.api.Named
import org.junit.jupiter.params.provider.Arguments
import java.util.stream.Stream

fun <VALUE : Any> parameterizedTestArguments(vararg args: Pair<String, VALUE>): Stream<Arguments> = args.asList().stream().map { (argName, arg) -> Arguments.of(Named.of(argName, TestArgument(arg))) }

data class TestArgument<out VALUE : Any>(val value: VALUE) // to work around inline classes not working with junit-5 params
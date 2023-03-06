package com.bol.kalaha.api.logger

import org.slf4j.Logger
import spock.lang.Specification
import spock.lang.Unroll

class RzLoggerSpec extends Specification {
    @Unroll
    def "Check trace"() {
        setup:
        Logger logger = Mock()
        RzLogger mockedDpLogger = new RzLogger(logger)

        when:
        mockedDpLogger.trace(message, *params)

        then:
        1 * logger.isTraceEnabled() >> true
        1 * logger.trace(*_) >> { arguments ->
            Object[] logParams = arguments[1..arguments.size() - 1]
            assert expectedParams.size() == logParams.size()
            for (int i = 0; i++; i < logParams.size()) {
                if (logParams[i] instanceof Throwable) {
                    assert expectedParams[i].class == logParams[i].class
                } else {
                    assert expectedParams[i] == logParams[i]
                }
            }
        }

        where:
        message | params                                  | expectedParams
        "a"     | [new RuntimeException()]                | [[new RuntimeException()]]
        "a"     | ["b"]                                   | [["b"]]
        "a"     | ["b", new RuntimeException()]           | [["b", new RuntimeException()]]
        "a"     | ["b", "c", new RuntimeException()]      | [["b", "c", new RuntimeException()]]
        "a"     | ["b", "c", "d", new RuntimeException()] | [["b", "c", "d", new RuntimeException()]]
    }

    @Unroll
    def "Check debug"() {
        setup:
        Logger logger = Mock()
        RzLogger mockedDpLogger = new RzLogger(logger)

        when:
        mockedDpLogger.debug(message, *params)

        then:
        1 * logger.isDebugEnabled() >> true
        1 * logger.debug(*_) >> { arguments ->
            Object[] logParams = arguments[1..arguments.size() - 1]
            assert expectedParams.size() == logParams.size()
            for (int i = 0; i++; i < logParams.size()) {
                if (logParams[i] instanceof Throwable) {
                    assert expectedParams[i].class == logParams[i].class
                } else {
                    assert expectedParams[i] == logParams[i]
                }
            }
        }

        where:
        message | params                                  | expectedParams
        "a"     | [new RuntimeException()]                | [[new RuntimeException()]]
        "a"     | ["b"]                                   | [["b"]]
        "a"     | ["b", new RuntimeException()]           | [["b", new RuntimeException()]]
        "a"     | ["b", "c", new RuntimeException()]      | [["b", "c", new RuntimeException()]]
        "a"     | ["b", "c", "d", new RuntimeException()] | [["b", "c", "d", new RuntimeException()]]
    }

    @Unroll
    def "Check info"() {
        setup:
        Logger logger = Mock()
        RzLogger mockedDpLogger = new RzLogger(logger)

        when:
        mockedDpLogger.info(message, *params)

        then:
        1 * logger.isInfoEnabled() >> true
        1 * logger.info(*_) >> { arguments ->
            Object[] logParams = arguments[1..arguments.size() - 1]
            assert expectedParams.size() == logParams.size()
            for (int i = 0; i++; i < logParams.size()) {
                if (logParams[i] instanceof Throwable) {
                    assert expectedParams[i].class == logParams[i].class
                } else {
                    assert expectedParams[i] == logParams[i]
                }
            }
        }

        where:
        message | params                                  | expectedParams
        "a"     | [new RuntimeException()]                | [[new RuntimeException()]]
        "a"     | ["b"]                                   | [["b"]]
        "a"     | ["b", new RuntimeException()]           | [["b", new RuntimeException()]]
        "a"     | ["b", "c", new RuntimeException()]      | [["b", "c", new RuntimeException()]]
        "a"     | ["b", "c", "d", new RuntimeException()] | [["b", "c", "d", new RuntimeException()]]
    }

    @Unroll
    def "Check warn"() {
        setup:
        Logger logger = Mock()
        RzLogger mockedDpLogger = new RzLogger(logger)

        when:
        mockedDpLogger.warn(message, *params)

        then:
        1 * logger.isWarnEnabled() >> true
        1 * logger.warn(*_) >> { arguments ->
            Object[] logParams = arguments[1..arguments.size() - 1]
            assert expectedParams.size() == logParams.size()
            for (int i = 0; i++; i < logParams.size()) {
                if (logParams[i] instanceof Throwable) {
                    assert expectedParams[i].class == logParams[i].class
                } else {
                    assert expectedParams[i] == logParams[i]
                }
            }
        }

        where:
        message | params                                  | expectedParams
        "a"     | [new RuntimeException()]                | [[new RuntimeException()]]
        "a"     | ["b"]                                   | [["b"]]
        "a"     | ["b", new RuntimeException()]           | [["b", new RuntimeException()]]
        "a"     | ["b", "c", new RuntimeException()]      | [["b", "c", new RuntimeException()]]
        "a"     | ["b", "c", "d", new RuntimeException()] | [["b", "c", "d", new RuntimeException()]]
    }

    @Unroll
    def "Check error"() {
        setup:
        Logger logger = Mock()
        RzLogger mockedDpLogger = new RzLogger(logger)

        when:
        mockedDpLogger.error(message, *params)

        then:
        1 * logger.isErrorEnabled() >> true
        1 * logger.error(*_) >> { arguments ->
            Object[] logParams = arguments[1..arguments.size() - 1]
            assert expectedParams.size() == logParams.size()
            for (int i = 0; i++; i < logParams.size()) {
                if (logParams[i] instanceof Throwable) {
                    assert expectedParams[i].class == logParams[i].class
                } else {
                    assert expectedParams[i] == logParams[i]
                }
            }
        }

        where:
        message | params                                  | expectedParams
        "a"     | [new RuntimeException()]                | [[new RuntimeException()]]
        "a"     | ["b"]                                   | [["b"]]
        "a"     | ["b", new RuntimeException()]           | [["b", new RuntimeException()]]
        "a"     | ["b", "c", new RuntimeException()]      | [["b", "c", new RuntimeException()]]
        "a"     | ["b", "c", "d", new RuntimeException()] | [["b", "c", "d", new RuntimeException()]]
    }

    @Unroll
    def "Check escaping unsafe characters"() {
        setup:
        RzLogger dpLogger = RzLogger.getLogger(RzLoggerSpec.class)

        when:
        String result = dpLogger.filterValues(source)[0]

        then:
        result == expected

        where:
        source        | expected
        "a"           | "a"
        "abc"         | "abc"
        "a<bc>d"      | "a&lt;bc&gt;d"
        "a<bc></bc>d" | "a&lt;bc&gt;&lt;/bc&gt;d"
        "a\r\nb"      | "a&cr;&lf;b"
        "a\n\rb"      | "a&lf;&cr;b"
        "a\rb"        | "a&cr;b"
        "a\nb"        | "a&lf;b"
    }

    @Unroll
    def "Check escaping sensitive data"() {
        setup:
        RzLogger dpLogger = RzLogger.getLogger(RzLoggerSpec.class)

        when:
        String result = dpLogger.filterValues(source)[0]

        then:
        result == expected

        where:
        source                         | expected
        "name NAME SURNAME"            | "name **** *******"
        "phone 994555555555"           | "phone **********"
        "tin 1234567890"               | "tin **********"
        "fin 9AA9AA9"                  | "fin *******"
        "email test@test.test"         | "email *@*.*"
        "amount 99.99"                 | "amount *.*"
        "card number 1234123412341234" | "card number ****************"
    }
}

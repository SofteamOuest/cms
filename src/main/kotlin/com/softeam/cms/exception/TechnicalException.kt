package com.softeam.cms.exception

import java.lang.RuntimeException

class TechnicalException(message: String?, cause: Throwable?) : RuntimeException(message, cause)
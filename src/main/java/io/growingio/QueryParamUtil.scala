package io.growingio

object QueryParamUtil {

  val REG_RULES = Seq("^__(.+)__$", "^\\{(.+)\\}$", "^\\[(.+)\\]$").map(_.r)

  def macroNotReplaced_Regex(value: String): Boolean = {
    REG_RULES.map(r => r.findFirstIn(value).isDefined).exists(p => p)
  }

  val MACRO_RULES = Seq(("__", "__"), ("{", "}"), ("[", "]"), ("${", "}"))

  def macroNotReplaced(value: String): Boolean = {
    MACRO_RULES.exists(rule => value.startsWith(rule._1) && value.endsWith(rule._2)
      && value.length >= rule._1.length + rule._2.length)
  }

}
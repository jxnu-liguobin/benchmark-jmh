package io.growingio


/**
 * 原方法
 *
 * @author liguobin@growingio.com
 * @version 1.0,2019/11/26
 */
object OriginQueryParamUtil {

  val REG_RULES = Seq("^__(.+)__$", "^\\{(.+)\\}$", "^\\[(.+)\\]$").map(_.r)

  def macroNotReplaced_Regex(value: String): Boolean = {
    for (rule <- REG_RULES) {
      if (!rule.findFirstIn(value).getOrElse("").isEmpty) {
        return true
      }
    }
    false
  }

  val MACRO_RULES = Seq(("__", "__"), ("{", "}"), ("[", "]"), ("${", "}"))

  def macroNotReplaced(value: String): Boolean = {
    for (rule <- MACRO_RULES) {
      if (value.startsWith(rule._1)
        && value.endsWith(rule._2)
        && value.length >= rule._1.length + rule._2.length) {
        return true
      }
    }
    false
  }
}

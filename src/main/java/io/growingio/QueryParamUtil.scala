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


  /**
   * {{{
   * 1. 对于macroNotReplaced_Regex方法：
   *     // 如下map方法源码中，CanBuildFrom是builder的工厂特质，只有apply与apply(from)两个方法
   *     def map[B, That](f: A => B)(implicit bf: CanBuildFrom[Repr, B, That]): That = {
   *       def builder = { // 提取以将方法大小保持在35个字节以下，以便可以进行JIT内联
   *         val b = bf(repr)  // 每个集合都定义了canBuildFrom隐式参数和newBuilder方法
   *         b.sizeHint(this) // 若是数组，预测集合的大小
   *         b
   *     }
   *     val b = builder // 获得一个builder
   *     // Builder所有是一个基本特质。Builder可以通过使用“+=”添加内容来逐步构造集合，然后转换为所需的具有`result`的集合类型。
   *     for (x <- this) b += f(x) // 调用f函数时，完成map的功能（类型转化），并将内容添加到builder对象中。
   *     b.result // 执行此操作后，builder的内容将变成undefined。
   *   }
   *
   *   主要流程中的方法调用
   *             map
   *          /        \
   *      builder       for
   *       /  \         /  \
   *   bf   sizeHint  +=    f   (f为r.findFirstIn(value).isDefined) //进行匹配
   *    \               /
   *     \             /
   *      \           /
   *       \         /
   *         \      /
   *          result
   *            |
   *          exists   exists为(iterator.exists(p))
   *            |
   *  while (!res && hasNext) res = p(next()) // 找到了就退出
   *
   * object Seq extends SeqFactory[Seq] {
   *   implicit def canBuildFrom[A]: CanBuildFrom[Coll, A, Seq[A]] = ReusableCBF.asInstanceOf[GenericCanBuildFrom[A]]
   *   def newBuilder[A]: Builder[A, Seq[A]] = new ArrayBuffer
   * }
   *
   * 对apply()的所有调用都将转发给newBuilder方法
   * apply() -> newBuilder
   * apply(from) -> genericBuilder
   *
   *          对应Seq来说,builder实现就是获取ArrayBuffer的所有元素
   *          CanBuildFrom转发给这个工厂的newBuilder方法
   *
   * 2. 对于macroNotReplaced方法：
   *         exists  同上
   *          |
   *  while (!res && hasNext) res = p(next())   // 找到了就退出
   *                                  |
   *                                  p   p为(rule => value.startsWith(rule._1) && value.endsWith(rule._2) && value.length >= rule._1.length + rule._2.length)
   *                                /   \
   *                     seq.startsWith     seq.endsWith (endsWith为startsWith(suffix, value.length - suffix.value.length);)
   *                           |                |
   *         if (ta[to++] != pa[po++]) {       同左边
   *                return false;
   *          }
   *          //从头遍历判断，不等就直接退出。当最后一个才知道不等时，执行了一个完整的遍历
   *
   * }}}
   */

}
package com.youliao.commons.utils;

import java.lang.annotation.*;

/**
 * 自定义一个@Cache注解,被@Cache标记的走拦截器进行Redis缓存,
 *
 * @author Lenny
 * @create 2021/11/28 23:58
 * @Description:
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented //标记注解
public @interface Cache {

    /**
     * 缓存的时间,. 不设置的话,默认为60秒
     * String time()--是 属性,  注解里面 没有方法
     */
    String time() default "60";
}


/*
* 1.@Target注解，是专门用来限定某个自定义注解能够被应用在哪些Java元素上面的。它使用一个枚举类型定义如下：
*
* 2.@Retention
@Retention注解，翻译为持久力、保持力。即用来修饰自定义注解的生命力。
注解的生命周期有三个阶段：1、Java源文件阶段；2、编译到class文件阶段；
* 3、运行期阶段。同样使用了RetentionPolicy枚举类型定义了三个阶段：
*       如果一个注解被定义为
*               RetentionPolicy.SOURCE，则它将被限定在Java源文件中，那么这个注解即不会参与编译也不会在运行期起任何作用，这个注解就和一个注释是一样的效果，只能被阅读Java文件的人看到；
                RetentionPolicy.CLASS，则它将被编译到Class文件中，那么编译器可以在编译时根据注解做一些处理动作，但是运行时JVM（Java虚拟机）会忽略它，我们在运行期也不能读取到；
                RetentionPolicy.RUNTIME，那么这个注解可以在运行期的加载阶段被加载到Class对象中。那么在程序运行阶段，我们可以通过反射得到这个注解，并通过判断是否有这个注解或这个注解中属性的值，从而执行不同的程序代码段。我们实际开发中的自定义注解几乎都是使用的RetentionPolicy.RUNTIME；
                在默认的情况下，自定义注解是使用的RetentionPolicy.CLASS。

   4.@Documented
        @Documented注解，是被用来指定自定义注解是否能随着被定义的java文件生成到JavaDoc文档当中。

   5. @Inherited
      @Inherited注解，是指定某个自定义注解如果写在了父类的声明部分，那么子类的声明部分也能自动拥有该注解。@Inherited注解只对那些@Target被定义为ElementType.TYPE的自定义注解起作用。


    *
    *
    * 注解保持力的三个阶段：
            Java源文件阶段；
                1.编译到class文件阶段；
                2.运行期阶段。
                3.只有当注解的保持力处于运行阶段，即使用@Retention(RetentionPolicy.RUNTIME)修饰注解时，
                    才能在JVM运行时，检测到注解，并进行一系列特殊操作。
                    *
                    *
       内部通过 "反射" 调用的
*/

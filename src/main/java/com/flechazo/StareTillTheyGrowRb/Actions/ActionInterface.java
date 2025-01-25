package com.flechazo.StareTillTheyGrowRb.Actions;

/**
 * 动作接口，定义了所有动作必须实现的方法。
 *
 * @author Flechazo
 * @since 1.20.1
 * @version 3.0.0
 */
public interface ActionInterface {

     /**
      * 执行当前动作。
      *
      * <p>该方法需要被实现以定义具体动作的行为。</p>
      */
     void invoke();
}
# MVPDemo
这是一个用mvp架构写的demo，实现了闪屏页的广告展示，版本更新弹窗、以及首页的Tab和带下拉刷新与上拉加载更多的列表页面

周一上线了一版，这些天在讨论下一波需求。正好前阵子有一位朋友问我关于mvp架构的问题，所以想写一篇博客来讲讲mvp。
之前刚接触mvp的时候，看了很多个版本，正所谓一百个人中就有一百个想法，但总感觉好像都不是我想要的。可能是我没找到写得好的吧。

看过很多mvp版本后，基于对它的理解，写了自己觉得比较接近它的定义的mvp模式。现在整理出一个demo发出来供大家参考。来看看图吧： 

 ![image](https://github.com/weioule/MVPDemo/blob/master/app/img/pic01.jpg)
  &emsp;![image](https://github.com/weioule/MVPDemo/blob/master/app/img/pic02.jpg)
  <br>ps: 这是闪屏页的广告，进入闪屏页会有1.75秒钟的加载时间，若数据在1.75秒内返回并且有广告，则显示广告及倒计时，否则直接进入首页。
  
  
   <br>![image](https://github.com/weioule/MVPDemo/blob/master/app/img/pic03.jpg)
     &emsp;![image](https://github.com/weioule/MVPDemo/blob/master/app/img/pic05.jpg)
     <br>ps: 这就是首页的版本更新弹窗与列表显示，带下拉刷新和加载更多。              

<br>其实mvp就是把传统的mvc分成三个模块，model数据模型层、view视图层 和 presenter逻辑处理层。
    <br>&emsp;&emsp;&emsp;model：作用是获取数据功能，用于网络接口的请求与模型解析。
    <br>&emsp;&emsp;&emsp;view ：主要是与用户交互的也页面，平时我们所展示的activity及fragment页面，就是管理UI展示的。
    <br>&emsp;&emsp;&emsp;presenter：主要负责view层与model层之间的数据传递，从view层发出的数据需求起，到model层请求后的数据返回，它就是一个中间件。但它不单单只是一个中间件，它还要处理view层页面展示的一些逻辑，而view页面只需要处理与UI相关就可以了。

<br>mvp这样分的好处就是：
    <br>&emsp;&emsp;&emsp;1 模型与视图完全分离，降低耦合度，完全符合高类聚低耦合的思想。
    <br>&emsp;&emsp;&emsp;2 代码更简洁，可阅读性高，利于他人维护和拓展，降低成本。
    
万事无绝对，有利也有弊，它的缺点就是：
    <br>&emsp;&emsp;&emsp;1 同时你要写很多个类，因为基本上每个页面就是有自己的model、view、presenter三个模块类组成。当然了，你也可以不这么写，你要是觉得类太多太麻烦，那你都写在一个类里也可以，你开心就好。但我就担心多年后的你再看到你当初写的代码，估计连你自己都不认识了，所以这就无形中填增了维护的成本。
    <br>&emsp;&emsp;&emsp;2 还有的就是因为涉及到view层与presenter层的交互，要注意实例的持有，如果的App比较复杂，页面比较多或者层级比较深的，一不小心就内存泄漏了，因为如果presenter层有延时的操作，尤其是handler和定时器等，一直持有view层的实例，那么本来要回收的页面不能被回收，堆积多了，那就造成内存泄漏了。
    
<br>刚开始我是在presenter的基类里使用弱引用去接收view层的实例的，但后来跑小米测试的时候，过不去，连软引用都过不去，所以就使用接口回调来做数据的传递，在view页面回收的时候再将它们断开。

好了，关于实现细节我这里就不多说，一切都在代码里。

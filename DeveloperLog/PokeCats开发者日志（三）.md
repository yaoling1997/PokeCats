&emsp;&emsp;现在是PokeCats游戏开发的第四天的晚上，明天要过周末了，所以提前写一下开发者日志吧！
##day4
&emsp;&emsp;day4主要是优化界面和增加游戏可玩性。
&emsp;&emsp;(1)感觉只有三只喵喵的话，玩家只需要无脑点屏幕就好了，为了增加游戏趣味性，我特意添加了炸弹事件，玩家如果不小心按到炸弹会减少生命值。实测发现玩起来确实更加需要技巧了！

&emsp;&emsp;(2)将相邻两帧动画的间隔从120ms调整到100ms。

&emsp;&emsp;(3)本来在真机上可以跑的，突然心血来潮想放到模拟机上跑，发现了这个。
<img src="https://i.loli.net/2018/01/26/5a6b47704c597.png" alt="5.png" title="5.png" />
报出了oom错误，貌似是bitmap用到的空间不够了，上网查了一下，[这个](http://blog.csdn.net/wangwangli6/article/details/54944235)还讲得不错，我直接偷懒地在AndroidManifest中加上了  android:largeHeap="true"。

&emsp;&emsp;成果展示：
<img src="https://i.loli.net/2018/01/26/5a6b4770e7f8b.jpg" alt="1.jpg" title="1.jpg"  style="zoom:30%"/>
<img src="https://i.loli.net/2018/01/26/5a6b47721e50d.jpg" alt="2.jpg" title="2.jpg"  style="zoom:30%"/>
<img src="https://i.loli.net/2018/01/26/5a6b4772677a6.jpg" alt="3.jpg" title="3.jpg"  style="zoom:30%"/>
<img src="https://i.loli.net/2018/01/26/5a6b47755761e.jpg" alt="4.jpg" title="4.jpg"  style="zoom:30%"/>
<img src="https://i.loli.net/2018/01/26/5a6b477968225.png" alt="7.png" title="7.png"  style="zoom:67%"/>
<img src="https://i.loli.net/2018/01/26/5a6b47794a00d.png" alt="6.png" title="6.png"  style="zoom:67%"/>

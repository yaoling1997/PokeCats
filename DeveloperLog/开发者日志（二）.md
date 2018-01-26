&emsp;&emsp;现在是PokeCats游戏开发的第四天的上午，来记录一下昨天做的事情吧。
##day3
&emsp;&emsp;day3主要是添加音效和优化界面，本以为添加个音效1~2个小时就够了吧，没想到贼不顺，弄了一个下午才搞好。
&emsp;&emsp;android播放音效主要是用到一个叫作[SoundPool](http://www.runoob.com/w3cnote/android-tutorial-soundpool.html)的东西。一开始用的是直接new SoundPool的方法，测试一下发现声音根本没有。最后发现Android 5.0后这个方法貌似不管用了，得用SoundPool.Builder才行。直接用了发现还是不行，setAudioAttributes中貌似不能填null，改成AudioManager.STREAM_MUSIC就好了。

&emsp;&emsp;然后就要准备音效啦，duang的声音直接从菜鸟教程的示例代码中抠出来用的，萌喵的叫声那就得从B站上截下来啦。。。作为一个码农既要会ps又要会处理声音真的好麻烦啊，我只想安安静静地写代码orz。
<img src="https://i.loli.net/2018/01/26/5a6a8a2ab96c8.png" alt="1.png" title="1.png" />

&emsp;&emsp;[Windows8.1下如何录制电脑内部播放的声音](https://jingyan.baidu.com/article/af9f5a2d37592c43140a45f3.html)呢？用电脑自带的录音机就好了，长得就像这个样子。
<img src="https://i.loli.net/2018/01/26/5a6a8acb7789e.png" alt="QQ截图20180126095613.png" title="QQ截图20180126095613.png" />
当然直接录是不行的，你还要调一下这个
<img src="https://i.loli.net/2018/01/26/5a6a8b36aa57f.png" alt="QQ截图20180126095736.png" title="QQ截图20180126095736.png"  style="zoom:80%"/>
把立体声混音打开。
&emsp;&emsp;截取了声音后可能还需要微调，这就需要用到酷狗了。打开工具，然后选择铃声制作。
<img src="https://i.loli.net/2018/01/26/5a6a8bae774cd.png" alt="QQ截图20180126095933.png" title="QQ截图20180126095933.png"  style="zoom:80%"/>
<img src="https://i.loli.net/2018/01/26/5a6a8bae7ebf5.png" alt="QQ截图20180126095945.png" title="QQ截图20180126095945.png"  style="zoom:80%"/>
&emsp;&emsp;然后发现截出来的声音太小了，喵叫根本听不到，于是我又用酷狗铃声制作专家把这个音频的声音增大了好几倍（一次最多增大两倍，要多倍的话多增大几次就行了）。
&emsp;&emsp;最后又优化了一下界面啥的，展示一下成果吧：
<img src="https://i.loli.net/2018/01/26/5a6a8d88c1696.jpg" alt="QQ图片20180126100757.jpg" title="QQ图片20180126100757.jpg"  style="zoom:30%"/>
<img src="https://i.loli.net/2018/01/26/5a6a8d88c34ea.jpg" alt="QQ图片20180126100753.jpg" title="QQ图片20180126100753.jpg"  style="zoom:30%"/>
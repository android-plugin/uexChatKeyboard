/**
 * Created by ylt on 16/8/24.
 */

if (UNIT_TEST) {
    var uexChatKeyboardCase = {
        "open":function(){
            var json ={
                "emojicons": "res://emojicons/emojicons.xml",
                "shares": "res://shares/shares.xml",
                "placeHold": "请输入内容",
                "touchDownImg": "res://1.png",
                "dragOutsideImg": "res://2.png",
                "textColor": "#FFF",
                "textSize": "15.5",
                "sendBtnbgColorUp": "#45C01A",
                "sendBtnbgColorDown": "#298409",
                "sendBtnText": "发送",
                "sendBtnTextSize": "15.5",
                "sendBtnTextColor": "#FFF",
                "maxLines":4,
                "inputMode":1
            };
            uexChatKeyboard.open(json);
            UNIT_TEST.assert(true);
        },
        "getInputBarHeight":function () {
            var result = uexChatKeyboard.getInputBarHeight();
            UNIT_TEST.log("InputBar 高度:"+result);
            if (result){
                UNIT_TEST.assert(true);
            }else{
                UNIT_TEST.assert(false);
            }
        },

        "onCommit":function () {
            var assert=false;
            uexChatKeyboard.onCommit = function(data){
                if (!assert){
                    UNIT_TEST.assertNotEqual(data,null);
                    assert=true;
                }
            };
            uexChatKeyboard.onKeyBoardShow = function(json){
                            //alert("onKeyBoardShow:"+json);
                            setTimeout(function (){window.scrollTo(0, document.body.scrollHeight);},20);
            }
        },
         "onKeyBoardDown":function () {
                                    var assert=false;
                                    uexChatKeyboard.onKeyBoardDown=function () {
                                        if (!assert){
                                            UNIT_TEST.assert(true);
                                            assert=true;
                                             alert(111111);
                                        }
                                    };

                                    UNIT_TEST.log("11111111111");
                                },
        "onCommitJson":function () {
            var assert=false;
            uexChatKeyboard.onCommitJson = function(data){

                if(!assert){
                    UNIT_TEST.log(JSON.stringify(data));
                    UNIT_TEST.assertNotEqual(data,null);
                    assert=true;
                }

            };
            UNIT_TEST.log("请发送文字");
        },
        "onShareMenuItem":function () {
            uexChatKeyboard.onShareMenuItem = function(data){
                UNIT_TEST.assertNotEqual(data,null);
            };
            UNIT_TEST.log("请点击分享按钮");
        },
        "onVoiceAction":function () {
            uexChatKeyboard.onVoiceAction = function(data){
                var json=JSON.parse(data);
                if (json.status==1){
                    UNIT_TEST.assert(true);
                }
            };
            UNIT_TEST.log("请录音");
        },
        "onKeyBoardShow":function () {
            var assert=false;
            uexChatKeyboard.onKeyBoardShow=function () {
                if (!assert){
                    UNIT_TEST.assert(true);
                    assert=true;
                }
            };
            UNIT_TEST.log("请展开键盘 输入@ 符号");
        },

        "onAt":function () {
            uexChatKeyboard.onAt=function () {
                UNIT_TEST.assert(true);
                UNIT_TEST.log("请输入@ 符号");
            };
        },
        "insertAfterAt":function () {
            uexChatKeyboard.onAt=function () {
                uexChatKeyboard.insertAfterAt("守望宝宝");
                UNIT_TEST.assert(true);
            };
        },
        "hideKeyboard":function () {
            uexChatKeyboard.hideKeyboard();
            UNIT_TEST.assert(true);
        },
        "close":function () {
            uexChatKeyboard.close();
            UNIT_TEST.assert(true);
        }
    };
    UNIT_TEST.addCase("uexChatKeyboard", uexChatKeyboardCase);
}
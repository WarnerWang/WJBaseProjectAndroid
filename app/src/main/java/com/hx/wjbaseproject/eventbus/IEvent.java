package com.hx.wjbaseproject.eventbus;


import com.hx.wjbaseproject.App;
import com.hx.wjbaseproject.api.token.TokenCache;

/**
 * eventBus 传递的事件
 */
public interface IEvent {

    class LoginSuccess {

        public LoginSuccess(){
        }
    }

    class Logout {
        public Logout(boolean reLogin) {
            this.reLogin = reLogin;
            TokenCache.getIns().setXingeToken(null);
        }

        public boolean reLogin = true;
    }

    class WeChatLoginSuccess {
        public WeChatLoginSuccess(){

        }
    }
}

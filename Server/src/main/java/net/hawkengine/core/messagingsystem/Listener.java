/*
 *   Copyright (C) 2016 R&D Solutions Ltd.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 *
 */

package net.hawkengine.core.messagingsystem;

import net.hawkengine.db.redis.RedisManager;
import redis.clients.jedis.Jedis;

import java.util.List;

class Listener implements Runnable {
    private List<String> list;

    @Override
    public void run() {
        int g =5;
        Jedis jedisSubscriber = RedisManager.getJedisPool().getResource();
        this.list.add("");
        jedisSubscriber.subscribe(new Subscriber(), MessagingSystem.DEFAULT_CHANNEL);
    }
}

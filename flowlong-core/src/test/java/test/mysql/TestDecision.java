/* Copyright 2023-2025 jobob@qq.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package test.mysql;

import com.flowlong.bpm.engine.RuntimeService;
import com.flowlong.bpm.engine.assist.StreamUtils;
import com.flowlong.bpm.engine.entity.Instance;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * TestDecision
 *
 * @author W.d
 **/
class TestDecision extends MysqlTest {

    /**
     * 使用decision的expr属性决定后置路线
     **/
    @Test
    void taskByDecisionExprTest() {
        processId = this.deployByResource("test/decision/decision-expr-process.long");
        Map<String, Object> args = new HashMap<>(16);
        args.put("task1.assignee", new String[]{"1", "2"});
        // args.put("task2.assignee", new String[]{"1","2"});
        // args.put("task3.assignee", new String[]{"1","2"});
        args.put("content", "toTask1");
        Instance instance = flowLongEngine.startInstanceById(processId, "testUser", args);
        System.out.println("instance = " + instance);
        Assertions.assertNotNull(instance);
        this.operCC(instance);
    }

    /**
     * 使用transition的expr属性决定后置路线
     **/
    @Test
    void taskByTransitionExprTest() {
        processId = this.deployByResource("test/decision/transition-expr-process.long");
        Map<String, Object> args = new HashMap<>();
        args.put("task1.assignee", new String[]{"1"});
        args.put("task2.assignee", new String[]{"1"});
        args.put("task3.assignee", new String[]{"1"});
        args.put("content", 250);
        Instance instance = flowLongEngine.startInstanceById(processId, "2", args);
        System.out.println("instance = " + instance);
        Assertions.assertNotNull(instance);
        this.operCC(instance);
    }

    /**
     * 使用decision的handler属性决定后置路线
     **/
    @Test
    void taskByDecisionHandlerTest() {
        processId = this.deploy(StreamUtils.getResourceAsStream("test/decision/decision-handler-process.long"), "1", false);
        Map<String, Object> args = new HashMap<>();
        args.put("task1.assignee", new String[]{"1"});
        args.put("task2.assignee", new String[]{"1"});
        args.put("task3.assignee", new String[]{"1"});
        args.put("content", "toTask3");
        Instance instance = flowLongEngine.startInstanceById(processId, "2", args);
        System.out.println("instance = " + instance);
        Assertions.assertNotNull(instance);
        this.operCC(instance);
    }

    private void operCC(Instance instance) {
        RuntimeService runtimeService = flowLongEngine.runtimeService();
        final String actorId = "1000";
        runtimeService.createCCInstance(instance.getId(), "test", actorId);
        runtimeService.deleteCCInstance(instance.getId(), actorId);
    }
}

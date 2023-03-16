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
package test.mysql.task.countersign;

import com.flowlong.bpm.engine.assist.Assert;
import com.flowlong.bpm.engine.entity.Instance;
import com.flowlong.bpm.engine.entity.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import test.mysql.MysqlTest;

import java.util.List;

/**
 * 测试会签任务
 * @author lijing
 * @since 1.0
 */
public class TestCountersignTask extends MysqlTest {

    @BeforeEach
    public void before() {
        processId = this.deployByResource("test/countersign/countersign.long");
    }

    /**
     * 简单会签测试
     */
    @Test
    public void test() {
        Instance instance = flowLongEngine.startInstanceById(processId, "2");
        List<Task> tasks = flowLongEngine.queryService().getActiveTasksByInstanceId(instance.getId());
        for (Task task : tasks) {
            flowLongEngine.executeTask(task.getId(), "2");
        }
    }

    /**
     * 动态加签测试
     */
    @Test
    public void test1() {
        Instance instance = flowLongEngine.startInstanceById(processId, "2");
        List<Task> tasks = flowLongEngine.queryService().getActiveTasksByInstanceId(instance.getId());
        Task task = tasks.get(0);
        Long taskId = task.getId();
        String actors = "123";
        String actors1 = "456";
        flowLongEngine.taskService().addTaskActor(taskId, actors, actors1);
        List<Task> newtasks = flowLongEngine.queryService().getActiveTasksByInstanceId(instance.getId());
        Assert.isTrue((tasks.size() + 2) == newtasks.size());
    }
}

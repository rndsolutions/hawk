/*
 * Copyright (C) 2016 R&D Solutions Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.hawkcd.agent.models;

import io.hawkcd.agent.models.payload.AgentInfo;

import java.util.ArrayList;

public class Environment {
    private String id;
    private String environmentName;
    private ArrayList<EnvironmentVariable> environmentVariables;
    private ArrayList<String> pipelineNames;
    private ArrayList<AgentInfo> agents;

    public Environment() {
        this.setEnvironmentVariables(new ArrayList<>());
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEnvironmentName() {
        return this.environmentName;
    }

    public void setEnvironmentName(String value) {
        this.environmentName = value;
    }

    public ArrayList<EnvironmentVariable> getEnvironmentVariables() {
        return this.environmentVariables;
    }

    public void setEnvironmentVariables(ArrayList<EnvironmentVariable> value) {
        this.environmentVariables = value;
    }

    public ArrayList<String> getPipelineNames() {
        return this.pipelineNames;
    }

    public void setPipelineNames(ArrayList<String> value) {
        this.pipelineNames = value;
    }

    public ArrayList<AgentInfo> getAgents() {
        return this.agents;
    }

    public void setAgents(ArrayList<AgentInfo> value) {
        this.agents = value;
    }
}

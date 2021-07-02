/*
 * Copyright (c) 2020-2021. Open JumpCO
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */
package io.jumpco.open.kfsm.gradle

class KFSMVizGenParam {
    String className
    File input
    File outputPlantUml = null
    File outputAsciiDoc = null

    KFSMVizGenParam(String className, File input) {
        this.className = className
        this.input = input
    }

    KFSMVizGenParam(String className, File input, File outputPlantUml, File outputAsciiDoc) {
        this.className = className
        this.input = input
        this.outputPlantUml = outputPlantUml
        this.outputAsciiDoc = outputAsciiDoc
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false

        KFSMVizGenParam that = (KFSMVizGenParam) o

        if (className != that.className) return false
        if (input != that.input) return false
        if (outputAsciiDoc != that.outputAsciiDoc) return false
        if (outputPlantUml != that.outputPlantUml) return false

        return true
    }

    int hashCode() {
        int result
        result = className.hashCode()
        result = 31 * result + input.hashCode()
        result = 31 * result + (outputPlantUml != null ? outputPlantUml.hashCode() : 0)
        result = 31 * result + (outputAsciiDoc != null ? outputAsciiDoc.hashCode() : 0)
        return result
    }
}

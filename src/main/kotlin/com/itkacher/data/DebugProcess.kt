/**
 * Copyright 2018 LocaleBro.com [Ievgenii Tkachenko(gektor650@gmail.com)]
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
package com.itkacher.data

class DebugProcess(
        val pid: Int?,
        var packageName: String?,
        var clientDescription: String?) {

    fun getClientKey(): String {
        return "$packageName$clientDescription"
    }

    override fun toString(): String {
        return if(packageName == null && clientDescription == null) {
            "Process [$pid]"
        } else if(clientDescription?.isNotEmpty() == true) {
            "$clientDescription [$pid]"
        } else {
            "$packageName [$pid]"
        }
    }
}
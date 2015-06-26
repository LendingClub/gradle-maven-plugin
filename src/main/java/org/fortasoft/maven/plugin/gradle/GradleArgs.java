// Copyright (C) 2013 Rob Schoening - http://www.github.com/if6was9
// 
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//        http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.fortasoft.maven.plugin.gradle;

import java.util.Collection;

public enum GradleArgs {

	OFFLINE("o","offline");

	private String shortValue;
	private String longValue;

	GradleArgs(String shortValue) {
		this.shortValue = String.format("-%s", shortValue);
	}	

	GradleArgs(String shortValue, String longValue) {
		this(shortValue);
		this.longValue =  String.format("--%s", longValue);
	}

	public String getShortValue() {
		return shortValue;
	}

	public String getLongValue() {
		return longValue;
	}

	public boolean exists(Collection args) {
		boolean result = false;

		if (args != null) {
			// first check short value
			result = args.contains(shortValue);

			if (!result && longValue != null) {
				result = args.contains(shortValue);
			}
		}

		return result;
	}
}

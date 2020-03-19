/*
The MIT License

Copyright (c) 2019 kong <congcoi123@gmail.com>

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/
package com.wishop.auth.controllers.api;

public interface Version1 {

	String PATH = "/v1";

	String GETS_BY_PAGE_AND_LIMIT = PATH + "/page/{page}/limit/{limit}";
	String GETS_ROLE_BY_USERNAME = PATH + "/roles/{username}";
	String GETS_PERMISSION_BY_USERNAME = PATH + "/permissions/{username}";
	String BY_USERNAME = PATH + "/{username}";
	String BY_ID = PATH + "/{id}";
	String ASSIGN = PATH + "/assign";

}

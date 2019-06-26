/*
 *  Copyright (c) 2014-2019 Kumuluz and/or its affiliates
 *  and other contributors as indicated by the @author tags and
 *  the contributor list.
 *
 *  Licensed under the MIT License (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  https://opensource.org/licenses/MIT
 *
 *  The software is provided "AS IS", WITHOUT WARRANTY OF ANY KIND, express or
 *  implied, including but not limited to the warranties of merchantability,
 *  fitness for a particular purpose and noninfringement. in no event shall the
 *  authors or copyright holders be liable for any claim, damages or other
 *  liability, whether in an action of contract, tort or otherwise, arising from,
 *  out of or in connection with the software or the use or other dealings in the
 *  software. See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.kumuluz.ee.logs.dtos;

/**
 * @author Domen Gašperlin
 * @since 1.4.0
 */
public class Level {

    private final String name;

    public static final Level FATAL = new Level("FATAL");
    public static final Level ERROR = new Level("ERROR");
    public static final Level WARN = new Level("WARN");
    public static final Level INFO = new Level("INFO");
    public static final Level DEBUG = new Level("DEBUG");
    public static final Level TRACE = new Level("TRACE");

    public String getName() {
        return name;
    }

    private Level(String name) {
        if (name == null) {
            throw new NullPointerException();
        }
        this.name = name;
    }

}

/*
 * Copyright (c) [2016] [ <ether.camp> ]
 * This file is part of the ethereumJ library.
 *
 * The ethereumJ library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The ethereumJ library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with the ethereumJ library. If not, see <http://www.gnu.org/licenses/>.
 */
package org.tron.common.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtil {

  public static boolean recursiveDelete(String fileName) {
    File file = new File(fileName);
    if (file.exists()) {
      //check if the file is a directory
      if (file.isDirectory()) {
        if ((file.list()).length > 0) {
          for (String s : file.list()) {
            //call deletion of file individually
            recursiveDelete(fileName + System.getProperty("file.separator") + s);
          }
        }
      }

      file.setWritable(true);
      boolean result = file.delete();
      return result;
    } else {
      return false;
    }
  }

  public static boolean saveData(File priFile, String data, boolean append) {
    try {
      priFile.createNewFile();
      FileWriter fw = new FileWriter(priFile, append);
      BufferedWriter bw = new BufferedWriter(fw);
      bw.write(data);
      bw.flush();
      bw.close();
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }

    return true;
  }

  public static int readData(File file, char[] buf) {
    int len;
    try {
      FileReader fileReader = new FileReader(file);
      if (null == fileReader) {
        return 0;
      }
      BufferedReader bufRead = new BufferedReader(fileReader);
      len = bufRead.read(buf, 0, buf.length);
      bufRead.close();
    } catch (FileNotFoundException ex) {
      ex.printStackTrace();
      return 0;
    } catch (IOException ex) {
      ex.printStackTrace();
      return 0;
    }
    return len;
  }
}

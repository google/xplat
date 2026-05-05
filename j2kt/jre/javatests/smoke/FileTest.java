/*
 * Copyright 2022 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package smoke;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Random;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class FileTest {

  @Test
  public void testFile() throws IOException {
    File file = new File("foo");
    assertEquals("foo", file.getPath());
    assertTrue(file.getAbsolutePath().startsWith("/"));

    File baseDir =
        new File(System.getProperty("java.io.tmpdir") + "/" + new Random().nextInt() + "/foo/bar");
    File testDir = new File(baseDir.getPath() + "/baz");
    testDir.delete();
    assertFalse(testDir.exists());
    assertTrue(testDir.mkdirs());
    assertTrue(testDir.exists());

    File[] subDirs = baseDir.listFiles();
    assertNotNull(subDirs);
    assertEquals(1, subDirs.length);
    File testDir2 = subDirs[0];
    assertTrue(
        "File path '" + testDir2.getPath() + "' ends with /foo/bar/baz",
        testDir2.getPath().endsWith("/foo/bar/baz"));
    assertTrue(testDir2 + " exists", testDir2.exists());

    assertTrue(testDir.delete());

    File testFile = new File(baseDir, "file1");
    File testFile2 = new File(baseDir, "file2");
    assertFalse(testFile.exists());
    assertTrue(testFile.createNewFile());
    assertTrue(testFile.exists());
    assertFalse(testFile.isDirectory());
    assertTrue(testFile.renameTo(testFile2));
    assertFalse(testFile.exists());
    assertTrue(testFile2.exists());
    assertTrue(testFile2.delete());
  }

  @Test
  public void testLastModified() throws IOException {
    File baseDir = new File(System.getProperty("java.io.tmpdir") + "/" + new Random().nextInt());
    assertTrue(baseDir.mkdirs());
    File testFile = new File(baseDir, "testFile");

    long startTime = System.currentTimeMillis();
    assertTrue(testFile.createNewFile());
    long endTime = System.currentTimeMillis();

    long lastModified = testFile.lastModified();

    assertTrue("lastModified should be >= startTime - 1000", lastModified >= startTime - 1000);
    assertTrue("lastModified should be <= endTime + 1000", lastModified <= endTime + 1000);

    assertTrue(testFile.delete());
    assertTrue(baseDir.delete());
  }

  @Test
  public void testGetParent() {
    assertEquals("/foo", new File("/foo/bar").getParent());
    assertEquals("/", new File("/foo").getParent());
    assertEquals("foo", new File("foo/bar").getParent());
    assertNull(new File("foo").getParent());
    assertNull(new File("/").getParent());
  }
}

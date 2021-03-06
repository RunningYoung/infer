/*
 * Copyright (c) 2013- Facebook.
 * All rights reserved.
 */

package endtoend.objc;

import static org.hamcrest.MatcherAssert.assertThat;
import static utils.matchers.ResultContainsNoErrorInMethod.doesNotContain;
import static utils.matchers.ResultContainsExactly.containsExactly;

import com.google.common.collect.ImmutableList;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import java.io.IOException;

import utils.DebuggableTemporaryFolder;
import utils.InferException;
import utils.InferResults;
import utils.InferRunner;

public class PrematureNilTerminationTest {

  public static final String PREMATURE_NIL_FILE =
      "infer/tests/codetoanalyze/objc/errors/variadic_methods/premature_nil_termination.m";

  private static ImmutableList<String> inferCmd;

  public static final String PREMATURE_NIL_TERMINATION_ARGUMENT =
      "PREMATURE_NIL_TERMINATION_ARGUMENT";

  @ClassRule
  public static DebuggableTemporaryFolder folderNPD = new DebuggableTemporaryFolder();

  @BeforeClass
  public static void runInfer() throws InterruptedException, IOException {
    inferCmd = InferRunner.createObjCInferCommandWithMLBuckets(
        folderNPD,
        PREMATURE_NIL_FILE,
        "cf",
        true);
  }

  @Test
  public void whenInferRunsOnTestThenNoNPENotFound()
      throws InterruptedException, IOException, InferException {
    InferResults inferResults = InferRunner.runInferObjC(inferCmd);
    String[] expectedPNTAProcedures = {"nilInArrayWithObjects"};

    assertThat(
        "Only PNTA should be found", inferResults,
        containsExactly(
            PREMATURE_NIL_TERMINATION_ARGUMENT,
            PREMATURE_NIL_FILE,
            expectedPNTAProcedures));
  }
}

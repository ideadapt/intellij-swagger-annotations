import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

public class ApiOperationFoldingTest extends LightJavaCodeInsightFixtureTestCase {

    @ApiOperation(
            value = ""
    )
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "filter"
            )
    })
    public void testApiOperation() {
        doTest();
    }

    public void testApiImplicitParams() {
        doTest();
    }

    private void doTest() {
        myFixture.testFolding(getTestDataPath() + getTestName(true) + ".java");
    }

    @Override
    protected String getBasePath() {
        return "src/test/testData/";
    }

    @Override
    protected String getTestDataPath() {
        return getBasePath();
    }
}

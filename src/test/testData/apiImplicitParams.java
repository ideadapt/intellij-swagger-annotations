import io.swagger.annotations.ApiOperation;

public class AnnotationsTest {

  <fold text='@Param name: string'>@ApiImplicitParams({
          @ApiImplicitParam(name = "name", value = "User's name", dataType = "string", paramType = "query"),
  })</fold>
  void op1(){}
}

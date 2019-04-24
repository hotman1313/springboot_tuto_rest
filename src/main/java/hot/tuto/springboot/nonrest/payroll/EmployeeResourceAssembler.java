package hot.tuto.springboot.nonrest.payroll;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

@Component
class EmployeeResourceAssembler implements ResourceAssembler<Employee, Resource<Employee>>{

	@Override
	public Resource<Employee> toResource(Employee entity) {
		return new Resource<>(entity, 
				linkTo(methodOn(EmployeeController.class).one(entity.getId())).withSelfRel(),
				linkTo(methodOn(EmployeeController.class).all()).withRel("employees"));
		
	}

}

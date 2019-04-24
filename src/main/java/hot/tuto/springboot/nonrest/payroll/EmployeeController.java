package hot.tuto.springboot.nonrest.payroll;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmployeeController {

	private final EmployeeRepository repository;
	
	private final EmployeeResourceAssembler assembler;
	
	EmployeeController(EmployeeRepository repository_, 
			EmployeeResourceAssembler assembler_){
		repository = repository_;
		assembler = assembler_;
	}
	
	// Aggregate root
	
	@GetMapping("/employees")
	Resources<Resource<Employee>> all(){
		
		List<Resource<Employee>> employees = repository.findAll().stream()
				.map(assembler::toResource).collect(Collectors.toList());
		
		return new Resources<>(employees, 
				linkTo(methodOn(EmployeeController.class).all()).withSelfRel());
	}
	
	@PostMapping("/employees")
	Employee newEmployee(@RequestBody Employee newEmployee) {
		return repository.save(newEmployee);
	}
	
	// single item
	
	@GetMapping("/employees/{id}")
	Resource<Employee> one(@PathVariable Long id) {

	  Employee employee = repository.findById(id)
	    .orElseThrow(() -> new EmployeeNotFoundException(id));

	  return assembler.toResource(employee);
	}
	
	@PutMapping("/employees/{id}")
	Employee replacEmployee(@RequestBody Employee newEmployee, @PathVariable Long id) {
		return repository.findById(id)
			      .map(employee -> {
			        employee.setName(newEmployee.getName());
			        employee.setRole(newEmployee.getRole());
			        return repository.save(employee);
			      })
			      .orElseGet(() -> {
			        newEmployee.setId(id);
			        return repository.save(newEmployee);
			      });

	}
	
	  @DeleteMapping("/employees/{id}")
	  void deleteEmployee(@PathVariable Long id) {
	    repository.deleteById(id);
	  }
}

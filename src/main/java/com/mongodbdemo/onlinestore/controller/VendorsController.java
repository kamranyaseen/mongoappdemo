package com.mongodbdemo.onlinestore.controller;

import com.mongodbdemo.onlinestore.repositories.VendorsRepository;

import io.swagger.v3.oas.annotations.tags.Tag;

import com.mongodbdemo.onlinestore.models.Vendor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin("http://localhost:4200")
@RestController
@RequestMapping("/v1/app")
public class VendorsController {
	@Autowired
	VendorsRepository vendorRepository;

	@GetMapping("/vendors")
	public ResponseEntity<List<Vendor>> getAllVendors(@RequestParam(required = false) String vendorTitle) {
		try {
			List<Vendor> listOfvendors = new ArrayList<>();
			if (vendorTitle == null || vendorTitle.isEmpty()) {
				vendorRepository.findAll().forEach(listOfvendors::add);
			} else {
				vendorRepository.findByName(vendorTitle).forEach(listOfvendors::add);
			}

			if (listOfvendors.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(listOfvendors, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/vendors/{id}")
	public ResponseEntity<Vendor> getVendorById(@PathVariable("id") String id) {
		try {
			Optional<Vendor> vendorOptional = vendorRepository.findById(id);

			return new ResponseEntity<>(vendorOptional.get(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/vendors")
	public ResponseEntity<Vendor> addAVendorToLibrary(@RequestBody Vendor vendor) {
		try {
			List<Vendor> checkEmail = vendorRepository.findByEmail(vendor.getEmail());
			if (!checkEmail.isEmpty()) {
				return new ResponseEntity<>(vendor, HttpStatus.IM_USED);
			} else {
				Vendor createdvendor = vendorRepository
						.save(new Vendor(vendor.getName(), vendor.getDescription(), vendor.getEmail(), vendor.getImage(),
								vendor.getCategory(), vendor.getAddress(), vendor.getGeolocation()));
				return new ResponseEntity<>(createdvendor, HttpStatus.CREATED);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/vendors/{id}")
	public ResponseEntity<Vendor> updateAVendor(@PathVariable("id") String id, @RequestBody Vendor vendor) {
		Optional<Vendor> vendorOptional = vendorRepository.findById(id);

		if (vendorOptional.isPresent()) {
			Vendor updatedvendor = vendorOptional.get();
			updatedvendor.setName(vendor.getName());
			updatedvendor.setDescription(vendor.getDescription());
			updatedvendor.setEmail(vendor.getEmail());
			updatedvendor.setImage(vendor.getImage());
			updatedvendor.setCategory(vendor.getCategory());
			updatedvendor.setAddress(vendor.getAddress());
			updatedvendor.setGeolocation(vendor.getGeolocation());
			return new ResponseEntity<>(vendorRepository.save(updatedvendor), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/vendors/{id}")
	public ResponseEntity<HttpStatus> deleteAVendor(@PathVariable("id") String id) {
		try {
			vendorRepository.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}

package com.social.marketing.user.controller;

import com.social.marketing.user.entity.Address;
import com.social.marketing.user.model.AddressResponse;
import com.social.marketing.user.model.CreateAddressRequest;
import com.social.marketing.user.model.UpdateAddressRequest;
import com.social.marketing.user.service.AddressService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/storefront/addresses")
@RequiredArgsConstructor
public class StorefrontAddressController {

    private final AddressService addressService;

    @GetMapping
    public List<AddressResponse> getAddresses() {
        List<Address> addresses = addressService.getAddressesByCurrentUser();
        return addresses.stream()
                .map(this::convertToResponse)
                .toList();
    }

    @PostMapping
    public AddressResponse createAddress(@Valid @RequestBody CreateAddressRequest request) {
        Address address = addressService.createAddress(request);
        return convertToResponse(address);
    }

    @PutMapping("/{id}")
    public AddressResponse updateAddress(@PathVariable Long id, @Valid @RequestBody UpdateAddressRequest request) {
        Address address = addressService.updateAddress(id, request);
        return convertToResponse(address);
    }

    @DeleteMapping("/{id}")
    public void deleteAddress(@PathVariable Long id) {
        addressService.deleteAddress(id);
    }

    private AddressResponse convertToResponse(Address address) {
        return new AddressResponse(
                address.getId(),
                address.getContactName(),
                address.getPhone(),
                address.getAddressLine1(),
                address.getAddressLine2(),
                address.getCity(),
                address.getState(),
                address.getZipCode(),
                address.getCountry(),
                address.getIsDefault());
    }
}

package com.social.marketing.user.service.impl;

import com.social.marketing.exception.NotFoundException;
import com.social.marketing.user.entity.Address;
import com.social.marketing.user.entity.User;
import com.social.marketing.user.model.CreateAddressRequest;
import com.social.marketing.user.model.UpdateAddressRequest;
import com.social.marketing.user.repository.AddressRepository;
import com.social.marketing.user.service.AddressService;
import com.social.marketing.user.service.UserService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@AllArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserService userService;

    @Override
    public List<Address> getAddressesByCurrentUser() {
        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            throw new NotFoundException("User not found");
        }
        return addressRepository.findByUser(currentUser);
    }

    @Override
    public Address getAddressById(Long id) {
        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            throw new NotFoundException("User not found");
        }
        return addressRepository.findByIdAndUser(id, currentUser)
                .orElseThrow(() -> new NotFoundException("Address not found"));
    }

    @Override
    public Address createAddress(CreateAddressRequest request) {
        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            throw new NotFoundException("User not found");
        }

        Address address = new Address();
        address.setUser(currentUser);
        address.setContactName(request.contactName());
        address.setPhone(request.phone());
        address.setAddressLine1(request.addressLine1());
        address.setAddressLine2(request.addressLine2());
        address.setCity(request.city());
        address.setState(request.state());
        address.setZipCode(request.zipCode());
        address.setCountry(request.country());
        address.setIsDefault(request.isDefault() != null ? request.isDefault() : false);

        // If this is set as default, unset other default addresses
        if (address.getIsDefault()) {
            List<Address> existingAddresses = addressRepository.findByUser(currentUser);
            existingAddresses.forEach(addr -> addr.setIsDefault(false));
            addressRepository.saveAll(existingAddresses);
        }

        return addressRepository.save(address);
    }

    @Override
    public Address updateAddress(Long id, UpdateAddressRequest request) {
        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            throw new NotFoundException("User not found");
        }

        Address address = addressRepository.findByIdAndUser(id, currentUser)
                .orElseThrow(() -> new NotFoundException("Address not found"));

        if (StringUtils.isNotBlank(request.contactName())) {
            address.setContactName(request.contactName());
        }
        if (StringUtils.isNotBlank(request.phone())) {
            address.setPhone(request.phone());
        }
        if (StringUtils.isNotBlank(request.addressLine1())) {
            address.setAddressLine1(request.addressLine1());
        }
        if (request.addressLine2() != null) {
            address.setAddressLine2(request.addressLine2());
        }
        if (StringUtils.isNotBlank(request.city())) {
            address.setCity(request.city());
        }
        if (request.state() != null) {
            address.setState(request.state());
        }
        if (request.zipCode() != null) {
            address.setZipCode(request.zipCode());
        }
        if (StringUtils.isNotBlank(request.country())) {
            address.setCountry(request.country());
        }
        if (request.isDefault() != null) {
            address.setIsDefault(request.isDefault());

            // If this is set as default, unset other default addresses
            if (address.getIsDefault()) {
                List<Address> existingAddresses = addressRepository.findByUser(currentUser);
                existingAddresses.stream()
                        .filter(addr -> !addr.getId().equals(id))
                        .forEach(addr -> addr.setIsDefault(false));
                addressRepository.saveAll(existingAddresses);
            }
        }

        return addressRepository.save(address);
    }

    @Override
    public void deleteAddress(Long id) {
        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            throw new NotFoundException("User not found");
        }

        Address address = addressRepository.findByIdAndUser(id, currentUser)
                .orElseThrow(() -> new NotFoundException("Address not found"));

        addressRepository.delete(address);
    }
}

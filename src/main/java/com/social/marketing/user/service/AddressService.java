package com.social.marketing.user.service;

import com.social.marketing.user.entity.Address;
import com.social.marketing.user.model.CreateAddressRequest;
import com.social.marketing.user.model.UpdateAddressRequest;

import java.util.List;

public interface AddressService {

    List<Address> getAddressesByCurrentUser();

    Address getAddressById(Long id);

    Address createAddress(CreateAddressRequest request);

    Address updateAddress(Long id, UpdateAddressRequest request);

    void deleteAddress(Long id);
}

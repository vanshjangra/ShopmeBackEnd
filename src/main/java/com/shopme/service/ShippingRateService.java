package com.shopme.service;

import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import com.shopme.dao.CountryRepository;
import com.shopme.dao.ShippingRateRepository;
import com.shopme.entity.Country;
import com.shopme.entity.ShippingRate;
import com.shopme.exception.ShippingRateAlreadyExistsException;
import com.shopme.exception.ShippingRateNotFoundException;
import com.shopme.util.PagingAndSortingHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ShippingRateService {
    public static final int RATES_PER_PAGE = 10;

    @Autowired
    private ShippingRateRepository shipRepo;

    @Autowired
    private CountryRepository countryRepo;

    public void listByPage(int pageNum, PagingAndSortingHelper helper) {
        helper.listEntities(pageNum, RATES_PER_PAGE, shipRepo);
    }

    public List<Country> listAllCountries() {
        return countryRepo.findAllByOrderByNameAsc();
    }

    public void save(ShippingRate rateInForm) throws ShippingRateAlreadyExistsException {
        ShippingRate rateInDB = shipRepo.findByCountryAndState(
                rateInForm.getCountry().getId(), rateInForm.getState());
        boolean foundExistingRateInNewMode = rateInForm.getId() == null && rateInDB != null;
        boolean foundDifferentExistingRateInEditMode = rateInForm.getId() != null && rateInDB != null && !rateInDB.equals(rateInForm);

        if (foundExistingRateInNewMode || foundDifferentExistingRateInEditMode) {
            throw new ShippingRateAlreadyExistsException("There's already a rate for the destination "
                    + rateInForm.getCountry().getName() + ", " + rateInForm.getState());
        }
        shipRepo.save(rateInForm);
    }

    public ShippingRate get(Integer id) throws ShippingRateNotFoundException {
        try {
            return shipRepo.findById(id).get();
        }
        catch (NoSuchElementException ex) {
            throw new ShippingRateNotFoundException("Could not find shipping rate with ID " + id);
        }
    }

    public void updateCODSupport(Integer id, boolean codSupported) throws ShippingRateNotFoundException {
        Long count = shipRepo.countById(id);
        if (count == null || count == 0) {
            throw new ShippingRateNotFoundException("Could not find shipping rate with ID " + id);
        }

        shipRepo.updateCODSupport(id, codSupported);
    }

    public void delete(Integer id) throws ShippingRateNotFoundException {
        Long count = shipRepo.countById(id);
        if (count == null || count == 0) {
            throw new ShippingRateNotFoundException("Could not find shipping rate with ID " + id);

        }
        shipRepo.deleteById(id);
    }
}
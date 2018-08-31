package com.test.vegetable.business.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.test.vegetable.business.VegetableBagBusiness;
import com.test.vegetable.common.VegetableBagConstant;
import com.test.vegetable.dao.VegetableBagDao;
import com.test.vegetable.dto.VegetableBagDetails;
import com.test.vegetable.enums.ErrorCodeEnum;
import com.test.vegetable.enums.SupplierEnum;
import com.test.vegetable.exception.ApplicationException;

/**
 * This class having functionality related to vegetable bag.
 * 
 * @author apparao
 *
 */
@Service
public class VegetableBagBusinessImpl implements VegetableBagBusiness {

	@Autowired
	private VegetableBagDao vegetableBagDao;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.test.vegetable.business.VegetableBagBusiness#getVegetableBags(java.lang.
	 * Integer, java.lang.Integer)
	 */
	@Transactional(readOnly = true)
	@Override
	public List<VegetableBagDetails> getVegetableBags(Integer pageNo, Integer pageSize) {
		
		if (null == pageNo) {
			pageNo = 1;
		}

		if (null == pageSize || pageSize <= 0) {
			pageSize = 3;
		}

		pageNo = pageNo - 1;

		pageNo = pageNo < 0 ? 0 : pageNo;

		List<VegetableBagDetails> vegetableBagList =  vegetableBagDao.getVegetableBags(pageNo, pageSize);
		
		if(CollectionUtils.isEmpty(vegetableBagList)) {
			
			throw new ApplicationException(VegetableBagConstant.NO_BAG_FOUND,ErrorCodeEnum.NO_BAG_FOUND); 
		}
		
		return vegetableBagList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.test.vegetable.business.VegetableBagBusiness#saveVegetableBag(com.test.
	 * vegetable.dto.VegetableBagDetails)
	 */
	@Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
	@Override
	public VegetableBagDetails saveVegetableBag(VegetableBagDetails vegetableBagDetails) throws ApplicationException {
		
		if(!(vegetableBagDetails.getQuantity() >= 1 && vegetableBagDetails.getQuantity() <= 100 )) {
			throw new ApplicationException(VegetableBagConstant.INVALID_QUANITITY,ErrorCodeEnum.INVALID_QUANITITY);
		}
		
		if(!(vegetableBagDetails.getPrice() >= 1 && vegetableBagDetails.getPrice() <= 50 )) {
			throw new ApplicationException(VegetableBagConstant.INVALID_PRICE,ErrorCodeEnum.INVALID_PRICE);
		}
		
		if(!SupplierEnum.getSupplierName(vegetableBagDetails.getSupplierName())) {
			throw new ApplicationException(VegetableBagConstant.INVALID_SUPPLIER,ErrorCodeEnum.INVALID_SUPPLIER);
		}
		
		return vegetableBagDao.saveVegetableBag(vegetableBagDetails);
	}

}

package vn.iotstar.service.Impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;
import java.util.Optional;
import vn.iotstar.entity.CategoryEntity;
import vn.iotstar.repository.CategoryRepository;
import vn.iotstar.service.ICategoryService;


@Service
public class CategoryServiceImpl implements ICategoryService {

	@Autowired
	CategoryRepository categoryRepository;
	
	public CategoryServiceImpl(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	
	@Override
	public List<CategoryEntity> findByNameContaining(String name) {
		return categoryRepository.findByNameContaining(name);
	}

	
	@Override
	public Page<CategoryEntity> findByNameContaining(String name, Pageable pageable) {
		return categoryRepository.findByNameContaining(name, pageable);
	}

	
	@Override
	public <S extends CategoryEntity> S save(S entity) {
		if(entity.getCategoryId() == null) {
			return categoryRepository.save(entity);
		}else {
			Optional<CategoryEntity> opt = findById(entity.getCategoryId());
			if(opt.isPresent()) {
				if(StringUtils.isEmpty(entity.getName())) {
					entity.setName(opt.get().getName());
				}else {
					entity.setName(entity.getName());
				}
			}
		return categoryRepository.save(entity);
	}
}

	
	@Override
	public <S extends CategoryEntity> Optional<S> findOne(Example<S> example) {
		return categoryRepository.findOne(example);
	}

	
	@Override
	public List<CategoryEntity> findAll(Sort sort) {
		return categoryRepository.findAll(sort);
	}

	
	@Override
	public Page<CategoryEntity> findAll(Pageable pageable) {
		return categoryRepository.findAll(pageable);
	}

	
	@Override
	public List<CategoryEntity> findAllById(Iterable<Long> ids) {
		return categoryRepository.findAllById(ids);
	}

	
	@Override
	public Optional<CategoryEntity> findById(Long id) {
		return categoryRepository.findById(id);
	}

	
	@Override
	public long count() {
		return categoryRepository.count();
	}

	
	@Override
	public void deleteById(Long id) {
		categoryRepository.deleteById(id);
	}

	
	@Override
	public void delete(CategoryEntity entity) {
		categoryRepository.delete(entity);
	}

	
	@Override
	public void deleteAll() {
		categoryRepository.deleteAll();
	}


	@Override
	public List<CategoryEntity> findAll() {
		// TODO Auto-generated method stub
		return null;
	}
	
}

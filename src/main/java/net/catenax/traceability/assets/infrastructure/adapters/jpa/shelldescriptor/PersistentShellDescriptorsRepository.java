package net.catenax.traceability.assets.infrastructure.adapters.jpa.shelldescriptor;

import net.catenax.traceability.assets.domain.model.ShellDescriptor;
import net.catenax.traceability.assets.domain.ports.ShellDescriptorRepository;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;

@Component
public class PersistentShellDescriptorsRepository implements ShellDescriptorRepository {

	private final JpaShellDescriptorRepository repository;

	public PersistentShellDescriptorsRepository(JpaShellDescriptorRepository repository) {
		this.repository = repository;
	}

	@Override
	public List<ShellDescriptor> findAll() {
		return repository.findAll().stream()
			.map(this::toShellDescriptor)
			.toList();
	}

	@Override
	public void update(ShellDescriptor shellDescriptor) {
		repository.findByShellDescriptorId(shellDescriptor.shellDescriptorId()).ifPresent(entity -> {
			entity.setUpdated(ZonedDateTime.now());
			repository.save(entity);
		});
	}

	@Override
	public void saveAll(Collection<ShellDescriptor> values) {
		repository.saveAll(values.stream()
			.map(this::toNewEntity)
			.toList());
	}

	@Override
	public void removeOldDescriptors(ZonedDateTime now) {
		repository.deleteAllByUpdatedBefore(now);
	}

	@Override
	public void clean() {
		repository.deleteAll();
	}

	private ShellDescriptor toShellDescriptor(ShellDescriptorEntity descriptor) {
		return new ShellDescriptor(descriptor.getShellDescriptorId(), descriptor.getGlobalAssetId());
	}

	private ShellDescriptorEntity toNewEntity(ShellDescriptor descriptor) {
		return ShellDescriptorEntity.newEntity(descriptor.shellDescriptorId(), descriptor.globalAssetId());
	}

}

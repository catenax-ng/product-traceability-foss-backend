package net.catenax.traceability.assets.infrastructure.adapters.jpa.shelldescriptor;

import net.catenax.traceability.assets.domain.model.ShellDescriptor;
import net.catenax.traceability.assets.domain.ports.ShellDescriptorRepository;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

@Component
public class PersistentShellDescriptorsRepository implements ShellDescriptorRepository {

	private final JpaShellDescriptorRepository repository;

	public PersistentShellDescriptorsRepository(JpaShellDescriptorRepository repository) {
		this.repository = repository;
	}

	@Override
	public List<ShellDescriptor> findByBpn(String bpn) {
		return repository.findByBpn(bpn).stream()
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
	public void save(String bpn, Collection<ShellDescriptor> values) {

		repository.saveAll(values.stream()
			.map(this.toNewEntity(bpn))
			.toList());
	}

	@Override
	public void removeOldDescriptors(String bpn, ZonedDateTime now) {
		repository.deleteAllByBpnAndUpdatedBefore(bpn, now);
	}

	@Override
	public void clean() {
		repository.deleteAll();
	}

	private ShellDescriptor toShellDescriptor(ShellDescriptorEntity descriptor) {
		return new ShellDescriptor(descriptor.getShellDescriptorId(), descriptor.getGlobalAssetId(), descriptor.getRawDescriptor());
	}

	private Function<ShellDescriptor, ShellDescriptorEntity> toNewEntity(String bpn) {
		return (descriptor) -> ShellDescriptorEntity.newEntity(descriptor.shellDescriptorId(), descriptor.globalAssetId(), bpn, descriptor.rawDescriptor());
	}

}

create table registry_lookup_metrics (
    id int8 generated by default as identity,
    status int4,
    start_date timestamp,
    end_date timestamp,
	success_shell_descriptors_fetch_count int8,
	failed_shell_descriptors_fetch_count int8,
	primary key (id)
);
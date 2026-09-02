# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Files Present:** 39/80 (48.8%)
- **Function parity:** 266/1077 matched (target 428) — 24.7%
- **Class/type parity:** 127/216 matched (target 184) — 58.8%
- **Combined symbol parity:** 393/1293 matched (target 612) — 30.4%
- **Average inline-code cosine:** 0.22 (function body across 30 matched files)
- **Average documentation cosine:** 0.33 (doc text across 30 matched files)
- **Cheat-zeroed Files:** 10
- **Critical Issues:** 37 files with <0.60 function similarity

## Priority 1: Fix Incomplete High-Dependency Files

No incomplete high-dependency files detected.

## Priority 2: Port Missing High-Value Files

Critical missing files (>10 dependencies):

1. **benches.fmt** (10 deps)
   - Path: `tracing-subscriber/benches/fmt.rs`
   - Essential for 10 other files

## Detailed Work Items

Every matched file is listed below with function and type symbol parity.

### 1. filter.level

- **Target:** `filter.Level [PROVENANCE-FALLBACK]`
- **Similarity:** 0.65
- **Dependents:** 4
- **Priority Score:** 4000303.5
- **Functions:** 3/3 matched (target 9)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 3)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `filter/level.rs` vs expected `filter/level.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:filter/level.rs` vs expected `filter/level.rs`
- **Proposed provenance header:** `// port-lint: source filter/level.rs` (current: `// port-lint: source filter/level.rs`)
- **Proposed provenance header:** `// port-lint: tests filter/level.rs` (current: `// port-lint: tests filter/level.rs`)
- **Lint issues:** 2

### 2. fmt.writer

- **Target:** `writer.Writer [PROVENANCE-FALLBACK]`
- **Similarity:** 0.18
- **Dependents:** 2
- **Priority Score:** 2194308.2
- **Functions:** 8/27 matched
- **Missing functions:** `new`, `with_stderr`, `flush`, `fmt`, `write_vectored`, `write_all`, `write_fmt`, `none`, `some`, `from`, `test_writer`, `has_lines`, `custom_writer_closure`, `custom_writer_struct`, `custom_writer_mutex`, `combinators_level_filters`, `combinators_or_else`, `combinators_or_else_chain`, `combinators_and`
- **Types:** 16/16 matched (target 20)
- **Missing types:** _none_
- **Tests:** 0/9 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `fmt/writer.rs` vs expected `fmt/writer.rs`
- **Proposed provenance header:** `// port-lint: source fmt/writer.rs` (current: `// port-lint: source fmt/writer.rs`)
- **Lint issues:** 2

### 3. filter.targets

- **Target:** `filter.Targets [PROVENANCE-FALLBACK]`
- **Similarity:** 0.22
- **Dependents:** 1
- **Priority Score:** 1184107.9
- **Functions:** 20/36 matched (target 34)
- **Missing functions:** `interested`, `extend`, `from_iter`, `from_str`, `fmt`, `next`, `size_hint`, `expect_parse`, `expect_parse_ralith`, `expect_parse_level_directives`, `parse_ralith`, `parse_ralith_uc`, `parse_ralith_mixed`, `expect_parse_valid`, `print_sz`, `test_roundtrip`
- **Types:** 3/5 matched (target 4)
- **Missing types:** `Err`, `Item`
- **Tests:** 8/17 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `filter/targets.rs` vs expected `filter/targets.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:filter/targets.rs` vs expected `filter/targets.rs`
- **Proposed provenance header:** `// port-lint: source filter/targets.rs` (current: `// port-lint: source filter/targets.rs`)
- **Proposed provenance header:** `// port-lint: tests filter/targets.rs` (current: `// port-lint: tests filter/targets.rs`)
- **Lint issues:** 2

### 4. format.escape

- **Target:** `format.Escape [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 1
- **Priority Score:** 1030410.0
- **Functions:** 0/2 matched (target 1)
- **Missing functions:** `write_str`, `fmt`
- **Types:** 1/2 matched (target 1)
- **Missing types:** `Escape`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `fmt/format/escape.rs` vs expected `fmt/format/escape.rs`
- **Proposed provenance header:** `// port-lint: source fmt/format/escape.rs` (current: `// port-lint: source fmt/format/escape.rs`)
- **Lint issues:** 1

### 5. fmt.fmt_layer

- **Target:** `fmt.FmtLayer [PROVENANCE-FALLBACK]`
- **Similarity:** 0.10
- **Dependents:** 0
- **Priority Score:** 658009.1
- **Functions:** 11/71 matched (target 13)
- **Missing functions:** `map_event_format`, `writer`, `writer_mut`, `set_ansi`, `set_span_events`, `with_test_writer`, `log_internal_errors`, `map_writer`, `with_timer`, `without_time`, `with_span_events`, `with_file`, `with_line_number`, `with_thread_ids`, `with_thread_names`, `compact`, `pretty`, `json`, `flatten_event`, `with_current_span`, `with_span_list`, `fmt_fields`, `map_fmt_fields`, `default`, `make_ctx`, `as_writer`, `fmt`, `deref`, `on_new_span`, `on_record`, `on_enter`, `on_exit`, `on_close`, `downcast_raw`, `format_fields`, `lookup_current`, `current_span`, `parent_span`, `span_scope`, `event_scope`, `field_format`, `impls`, `fmt_layer_downcasts`, `fmt_layer_downcasts_to_parts`, `is_lookup_span`, `assert_lookup_span`, `sanitize_timings`, `format_error_print_to_stderr`, `format_error_ignore_if_log_internal_errors_is_false`, `synthesize_span_none`, `synthesize_span_active`, `synthesize_span_close`, `synthesize_span_close_no_timing`, `synthesize_span_full`, `make_writer_based_on_meta`, `make_writer`, `make_writer_for`, `layer_no_color`, `drop`, `modify_span_events`
- **Types:** 4/9 matched (target 4)
- **Missing types:** `Target`, `AlwaysError`, `MakeByTarget`, `Writer`, `RestoreEnvVar`
- **Tests:** 0/19 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `fmt/fmt_layer.rs` vs expected `fmt/fmt_layer.rs`
- **Proposed provenance header:** `// port-lint: source fmt/fmt_layer.rs` (current: `// port-lint: source fmt/fmt_layer.rs`)
- **Lint issues:** 2

### 6. fmt.mod

- **Target:** `fmt.Mod [STUB] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 617010.0
- **Functions:** 8/63 matched (target 18)
- **Missing functions:** `layer`, `builder`, `new`, `default`, `register_callsite`, `enabled`, `new_span`, `record`, `record_follows_from`, `event_enabled`, `event`, `enter`, `exit`, `current_span`, `clone_span`, `try_close`, `max_level_hint`, `downcast_raw`, `span_data`, `try_init`, `from`, `with_timer`, `without_time`, `with_span_events`, `log_internal_errors`, `with_file`, `with_line_number`, `with_thread_names`, `with_thread_ids`, `compact`, `pretty`, `json`, `flatten_event`, `with_current_span`, `with_span_list`, `with_filter_reloading`, `reload_handle`, `fmt_fields`, `with_env_filter`, `with_max_level`, `with_test_writer`, `map_event_format`, `map_fmt_fields`, `map_writer`, `map_error`, `buf`, `write`, `flush`, `get_string`, `make_writer`, `impls`, `subscriber_downcasts`, `subscriber_downcasts_to_parts`, `is_lookup_span`, `assert_lookup_span`
- **Types:** 1/7 matched (target 2)
- **Missing types:** `Subscriber`, `Formatter`, `Data`, `MockWriter`, `MockMakeWriter`, `Writer`
- **Tests:** 0/11 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `fmt/mod.rs` vs expected `fmt/mod.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:fmt/mod.rs` vs expected `fmt/mod.rs`
- **Proposed provenance header:** `// port-lint: source fmt/mod.rs` (current: `// port-lint: source fmt/mod.rs`)
- **Proposed provenance header:** `// port-lint: tests fmt/mod.rs` (current: `// port-lint: tests fmt/mod.rs`)
- **Lint issues:** 2

### 7. registry.sharded

- **Target:** `registry.Sharded [ZERO] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 555910.0
- **Functions:** 0/49 matched (target 0)
- **Missing functions:** `default`, `idx_to_id`, `id_to_idx`, `get`, `start_close`, `has_per_layer_filters`, `span_stack`, `register_callsite`, `enabled`, `new_span`, `record`, `record_follows_from`, `event_enabled`, `event`, `enter`, `exit`, `clone_span`, `current_span`, `try_close`, `span_data`, `register_filter`, `set_closing`, `drop`, `id`, `metadata`, `parent`, `extensions`, `extensions_mut`, `is_enabled_for`, `set_interest`, `clear`, `on_close`, `single_layer_can_access_closed_span`, `multiple_layers_can_access_closed_span`, `on_new_span`, `new`, `is_open`, `is_closed`, `assert_closed`, `assert_open`, `assert_removed`, `assert_not_removed`, `assert_last_closed`, `assert_closed_in_order`, `spans_are_removed_from_registry`, `spans_are_only_closed_when_the_last_ref_drops`, `span_enter_guards_are_dropped_out_of_order`, `child_closes_parent`, `child_closes_grandparent`
- **Types:** 4/10 matched (target 5)
- **Missing types:** `Registry`, `AssertionLayer`, `CloseLayer`, `CloseHandle`, `CloseState`, `SetRemoved`
- **Tests:** 0/18 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `registry/sharded.rs` vs expected `registry/sharded.rs`
- **Proposed provenance header:** `// port-lint: source registry/sharded.rs` (current: `// port-lint: source registry/sharded.rs`)
- **Lint issues:** 1

### 8. format.mod

- **Target:** `format.Mod [STUB] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 549110.0
- **Functions:** 20/71 matched (target 31)
- **Missing functions:** `json`, `debug_fn`, `new`, `with_ansi`, `by_ref`, `write_str`, `write_char`, `write_fmt`, `has_ansi_escapes`, `dimmed`, `italic`, `fmt`, `default`, `compact`, `pretty`, `with_thread_ids`, `with_thread_names`, `with_file`, `with_line_number`, `with_source_location`, `format_timestamp`, `flatten_event`, `with_current_span`, `with_span_list`, `make_visitor`, `maybe_pad`, `record_error`, `finish`, `writer`, `format_time`, `disable_everything`, `test_ansi`, `test_without_ansi`, `test_without_level`, `with_line_number_and_file_name`, `with_filename`, `pretty_default`, `assert_info_hello`, `assert_info_hello_ignore_numeric`, `test_overridden_parents`, `test_overridden_parents_in_scope`, `run_test`, `with_ansi_true`, `with_ansi_false`, `without_ansi`, `without_level`, `overridden_parents`, `overridden_parents_in_scope`, `format_nanos`, `fmt_span_combinations`, `current_path`
- **Types:** 17/20 matched (target 19)
- **Missing types:** `Writer`, `Visitor`, `MockTime`
- **Tests:** 0/22 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `fmt/format/mod.rs` vs expected `fmt/format/mod.rs`
- **Proposed provenance header:** `// port-lint: source fmt/format/mod.rs` (current: `// port-lint: source fmt/format/mod.rs`)
- **Lint issues:** 1

### 9. env.mod

- **Target:** `env.Mod [STUB] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 394810.0
- **Functions:** 6/41 matched (target 9)
- **Missing functions:** `clone`, `from_env`, `try_new`, `try_from_default_env`, `try_from_env`, `on_new_span`, `on_enter`, `on_exit`, `on_close`, `on_record`, `cares_about_span`, `base_interest`, `register_callsite`, `from_str`, `from`, `default`, `fmt`, `source`, `new_span`, `event`, `record`, `record_follows_from`, `enter`, `exit`, `set_interest`, `metadata`, `callsite_enabled_no_span_directive`, `callsite_off`, `callsite_enabled_includes_span_directive`, `callsite_enabled_includes_span_directive_field`, `callsite_enabled_includes_span_directive_multiple_fields`, `roundtrip`, `size_of_filters`, `print_sz`, `parse_empty_string`
- **Types:** 3/7 matched (target 6)
- **Missing types:** `FieldMap`, `Err`, `NoSubscriber`, `Cs`
- **Tests:** 0/17 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `filter/env/mod.rs` vs expected `filter/env/mod.rs`
- **Proposed provenance header:** `// port-lint: source filter/env/mod.rs` (current: `// port-lint: source filter/env/mod.rs`)
- **Lint issues:** 2

### 10. env.field

- **Target:** `env.Field [PROVENANCE-FALLBACK]`
- **Similarity:** 0.01
- **Dependents:** 0
- **Priority Score:** 314109.8
- **Functions:** 1/30 matched (target 2)
- **Missing functions:** `eq`, `cmp`, `partial_cmp`, `has_value`, `name`, `parse`, `fmt`, `value_match_f64`, `parse_regex`, `parse_non_regex`, `from_str`, `as_ref`, `str_matches`, `debug_matches`, `into_debug_match`, `new`, `to_span_match`, `visitor`, `is_matched`, `is_matched_slow`, `filter`, `record_f64`, `record_i64`, `record_u64`, `record_bool`, `record_str`, `record_debug`, `debug_struct_match`, `debug_struct_not_match`
- **Types:** 9/11 matched (target 16)
- **Missing types:** `Err`, `MyStruct`
- **Tests:** 0/2 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `filter/env/field.rs` vs expected `filter/env/field.rs`
- **Proposed provenance header:** `// port-lint: source filter/env/field.rs` (current: `// port-lint: source filter/env/field.rs`)
- **Lint issues:** 1

### 11. layer_filters.mod

- **Target:** `layerfilters.Mod [STUB] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 294910.0
- **Functions:** 13/41 matched (target 15)
- **Missing functions:** `new`, `and`, `or`, `not`, `boxed`, `callsite_enabled`, `id`, `did_enable`, `filter`, `filter_mut`, `inner`, `inner_mut`, `on_register_dispatch`, `on_layer`, `downcast_raw`, `fmt`, `disabled`, `none`, `is_disabled`, `set`, `any_enabled`, `add_interest`, `clear_enabled`, `take_interest`, `filter_map`, `is_plf_downcast_marker`, `subscriber_has_plf`, `layer_has_plf`
- **Types:** 7/8 matched (target 7)
- **Missing types:** `FilterExt`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `filter/layer_filters/mod.rs` vs expected `filter/layer_filters/mod.rs`
- **Proposed provenance header:** `// port-lint: source filter/layer_filters/mod.rs` (current: `// port-lint: source filter/layer_filters/mod.rs`)
- **Lint issues:** 1

### 12. format.json

- **Target:** `format.Json [PROVENANCE-FALLBACK]`
- **Similarity:** 0.12
- **Dependents:** 0
- **Priority Score:** 294208.8
- **Functions:** 9/36 matched (target 9)
- **Missing functions:** `flatten_event`, `with_current_span`, `with_span_list`, `serialize`, `default`, `add_fields`, `fmt`, `writer`, `finish`, `record_value`, `record_bytes`, `format_time`, `subscriber`, `json`, `json_filename`, `json_line_number`, `json_flattened_event`, `json_disabled_current_span_event`, `json_disabled_span_list_event`, `json_nested_span`, `json_no_span`, `record_works`, `json_span_event_show_correct_context`, `json_span_event_with_no_fields`, `parse_as_json`, `test_json`, `test_json_with_line_number`
- **Types:** 4/6 matched (target 5)
- **Missing types:** `Json`, `MockTime`
- **Tests:** 0/16 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `fmt/format/json.rs` vs expected `fmt/format/json.rs`
- **Proposed provenance header:** `// port-lint: source fmt/format/json.rs` (current: `// port-lint: source fmt/format/json.rs`)
- **Lint issues:** 1

### 13. filter.directive

- **Target:** `filter.Directive [PROVENANCE-FALLBACK]`
- **Similarity:** 0.08
- **Dependents:** 0
- **Priority Score:** 283409.2
- **Functions:** 3/25 matched (target 4)
- **Missing functions:** `is_empty`, `default`, `directives`, `directives_for`, `into_vec`, `from_iter`, `extend`, `into_iter`, `enabled`, `target_enabled`, `directives_for_target`, `new`, `cares_about_target`, `cmp`, `partial_cmp`, `level`, `fmt`, `from_str`, `msg`, `description`, `source`, `from`
- **Types:** 3/9 matched (target 3)
- **Missing types:** `FilterVec`, `Match`, `ParseErrorKind`, `Item`, `IntoIter`, `Err`
- **Tests:** 0/1 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `filter/directive.rs` vs expected `filter/directive.rs`
- **Proposed provenance header:** `// port-lint: source filter/directive.rs` (current: `// port-lint: source filter/directive.rs`)
- **Lint issues:** 1

### 14. env.directive

- **Target:** `env.Directive [PROVENANCE-FALLBACK]`
- **Similarity:** 0.21
- **Dependents:** 0
- **Priority Score:** 225607.9
- **Functions:** 29/48 matched (target 30)
- **Missing functions:** `to_static`, `is_static`, `field_matcher`, `make_tables`, `deregexify`, `level`, `from_str`, `default`, `partial_cmp`, `cmp`, `fmt`, `from`, `matcher`, `has_value_filters`, `to_span_match`, `record_update`, `parse_directives`, `expect_parse`, `test_parse_bare_level`
- **Types:** 5/8 matched (target 14)
- **Missing types:** `Dynamics`, `Statics`, `Err`
- **Tests:** 24/27 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `filter/env/directive.rs` vs expected `filter/env/directive.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:filter/env/directive.rs` vs expected `filter/env/directive.rs`
- **Proposed provenance header:** `// port-lint: source filter/env/directive.rs` (current: `// port-lint: source filter/env/directive.rs`)
- **Proposed provenance header:** `// port-lint: tests filter/env/directive.rs` (current: `// port-lint: tests filter/env/directive.rs`)
- **Lint issues:** 2

### 15. field.mod

- **Target:** `field.Mod [STUB] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 162810.0
- **Functions:** 6/14 matched (target 11)
- **Missing functions:** `record`, `make_visitor`, `with`, `set_interest`, `metadata`, `new`, `finish`, `writer`
- **Types:** 6/14 matched (target 7)
- **Missing types:** `Visitor`, `MakeExtMarker`, `RecordFieldsMarker`, `TestAttrs1`, `TestAttrs2`, `TestCallsite1`, `MakeDebug`, `DebugVisitor`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `field/mod.rs` vs expected `field/mod.rs`
- **Proposed provenance header:** `// port-lint: source field/mod.rs` (current: `// port-lint: source field/mod.rs`)
- **Lint issues:** 11

### 16. registry.mod

- **Target:** `registry.Mod [STUB] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 152310.0
- **Functions:** 4/17 matched (target 26)
- **Missing functions:** `next`, `id`, `metadata`, `name`, `fields`, `parent`, `extensions`, `extensions_mut`, `try_with_filter`, `with_filter`, `spanref_scope_iteration_order`, `on_enter`, `spanref_scope_fromroot_iteration_order`
- **Types:** 4/6 matched (target 9)
- **Missing types:** `Item`, `PrintingLayer`
- **Tests:** 0/2 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `registry/mod.rs` vs expected `registry/mod.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:registry/mod.rs` vs expected `registry/mod.rs`
- **Proposed provenance header:** `// port-lint: source registry/mod.rs` (current: `// port-lint: source registry/mod.rs`)
- **Proposed provenance header:** `// port-lint: tests registry/mod.rs` (current: `// port-lint: tests registry/mod.rs`)
- **Lint issues:** 5

### 17. format.pretty

- **Target:** `format.Pretty [PROVENANCE-FALLBACK]`
- **Similarity:** 0.14
- **Dependents:** 0
- **Priority Score:** 142108.6
- **Functions:** 5/17 matched (target 10)
- **Missing functions:** `default`, `style_for`, `with_source_location`, `add_fields`, `with_ansi`, `make_visitor`, `with_style`, `write_padded`, `bold`, `record_error`, `finish`, `writer`
- **Types:** 2/4 matched (target 3)
- **Missing types:** `Pretty`, `Visitor`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `fmt/format/pretty.rs` vs expected `fmt/format/pretty.rs`
- **Proposed provenance header:** `// port-lint: source fmt/format/pretty.rs` (current: `// port-lint: source fmt/format/pretty.rs`)
- **Lint issues:** 1

### 18. registry.extensions

- **Target:** `registry.Extensions [PROVENANCE-FALLBACK]`
- **Similarity:** 0.16
- **Dependents:** 0
- **Priority Score:** 132208.4
- **Functions:** 5/14 matched (target 17)
- **Missing functions:** `write`, `write_u64`, `finish`, `new`, `get_mut`, `fmt`, `test_extensions`, `clear_retains_capacity`, `clear_drops_elements`
- **Types:** 4/8 matched (target 4)
- **Missing types:** `AnyMap`, `MyType`, `DropMePlease`, `DropMeTooPlease`
- **Tests:** 0/3 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `registry/extensions.rs` vs expected `registry/extensions.rs`
- **Proposed provenance header:** `// port-lint: source registry/extensions.rs` (current: `// port-lint: source registry/extensions.rs`)
- **Lint issues:** 1

### 19. layer.layered

- **Target:** `layer.Layered [PROVENANCE-FALLBACK]`
- **Similarity:** 0.41
- **Dependents:** 0
- **Priority Score:** 123605.9
- **Functions:** 23/34 matched (target 29)
- **Missing functions:** `is`, `downcast_ref`, `on_register_dispatch`, `drop_span`, `downcast_raw`, `on_layer`, `ctx`, `new`, `pick_interest`, `pick_level_hint`, `fmt`
- **Types:** 1/2 matched (target 4)
- **Missing types:** `Data`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `layer/layered.rs` vs expected `layer/layered.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:layer/layered.rs` vs expected `layer/layered.rs`
- **Proposed provenance header:** `// port-lint: source layer/layered.rs` (current: `// port-lint: source layer/layered.rs`)
- **Proposed provenance header:** `// port-lint: tests layer/layered.rs` (current: `// port-lint: tests layer/layered.rs`)
- **Lint issues:** 2

### 20. tracing-subscriber.reload

- **Target:** `tracingsubscriber.Reload [PROVENANCE-FALLBACK]`
- **Similarity:** 0.30
- **Dependents:** 0
- **Priority Score:** 123107.0
- **Functions:** 16/27 matched (target 16)
- **Missing functions:** `on_register_dispatch`, `on_layer`, `downcast_raw`, `callsite_enabled`, `handle`, `clone_current`, `clone`, `poisoned`, `is_poisoned`, `is_dropped`, `fmt`
- **Types:** 3/4 matched (target 8)
- **Missing types:** `Layer`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `reload.rs` vs expected `reload.rs`
- **Proposed provenance header:** `// port-lint: source reload.rs` (current: `// port-lint: source reload.rs`)
- **Lint issues:** 1

### 21. filter.filter_fn

- **Target:** `filter.FilterFn [PROVENANCE-FALLBACK]`
- **Similarity:** 0.12
- **Dependents:** 0
- **Priority Score:** 111708.8
- **Functions:** 4/15 matched (target 12)
- **Missing functions:** `new`, `with_max_level_hint`, `is_enabled`, `is_callsite_enabled`, `is_below_max_level`, `register_callsite`, `from`, `fmt`, `with_callsite_filter`, `default_callsite_enabled`, `clone`
- **Types:** 2/2 matched (target 3)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `filter/filter_fn.rs` vs expected `filter/filter_fn.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:filter/filter_fn.rs` vs expected `filter/filter_fn.rs`
- **Proposed provenance header:** `// port-lint: source filter/filter_fn.rs` (current: `// port-lint: source filter/filter_fn.rs`)
- **Proposed provenance header:** `// port-lint: tests filter/filter_fn.rs` (current: `// port-lint: tests filter/filter_fn.rs`)
- **Lint issues:** 2

### 22. layer.context

- **Target:** `layer.Context [PROVENANCE-FALLBACK]`
- **Similarity:** 0.35
- **Dependents:** 0
- **Priority Score:** 91906.5
- **Functions:** 9/18 matched (target 10)
- **Missing functions:** `new`, `span`, `lookup_current_filtered`, `with_filter`, `is_enabled_for`, `if_enabled_for`, `is_enabled_inner`, `none`, `clone`
- **Types:** 1/1 matched
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `layer/context.rs` vs expected `layer/context.rs`
- **Proposed provenance header:** `// port-lint: source layer/context.rs` (current: `// port-lint: source layer/context.rs`)
- **Lint issues:** 1

### 23. layer_filters.combinator

- **Target:** `layerfilters.Combinator [PROVENANCE-FALLBACK]`
- **Similarity:** 0.19
- **Dependents:** 0
- **Priority Score:** 81508.1
- **Functions:** 4/12 matched
- **Missing functions:** `new`, `on_new_span`, `on_record`, `on_enter`, `on_exit`, `on_close`, `clone`, `fmt`
- **Types:** 3/3 matched
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `filter/layer_filters/combinator.rs` vs expected `filter/layer_filters/combinator.rs`
- **Proposed provenance header:** `// port-lint: source filter/layer_filters/combinator.rs` (current: `// port-lint: source filter/layer_filters/combinator.rs`)
- **Lint issues:** 1

### 24. layer.mod

- **Target:** `layer.Mod [STUB] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 72710.0
- **Functions:** 16/23 matched (target 17)
- **Missing functions:** `on_register_dispatch`, `on_layer`, `boxed`, `downcast_raw`, `layer_is_none`, `subscriber_is_none`, `new`
- **Types:** 4/4 matched
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `layer/mod.rs` vs expected `layer/mod.rs`
- **Proposed provenance header:** `// port-lint: source layer/mod.rs` (current: `// port-lint: source layer/mod.rs`)
- **Lint issues:** 21

### 25. env.builder

- **Target:** `env.Builder [PROVENANCE-FALLBACK]`
- **Similarity:** 0.21
- **Dependents:** 0
- **Priority Score:** 71207.9
- **Functions:** 4/11 matched (target 6)
- **Missing functions:** `with_env_var`, `from_env_lossy`, `from_env`, `try_from_env`, `from_directives`, `env_var_name`, `default`
- **Types:** 1/1 matched (target 2)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `filter/env/builder.rs` vs expected `filter/env/builder.rs`
- **Proposed provenance header:** `// port-lint: source filter/env/builder.rs` (current: `// port-lint: source filter/env/builder.rs`)
- **Lint issues:** 1

### 26. time.time_crate

- **Target:** `time.TimeCrate [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 60910.0
- **Functions:** 0/6 matched (target 3)
- **Missing functions:** `rfc_3339`, `new`, `format_time`, `default`, `local_rfc_3339`, `format_datetime`
- **Types:** 3/3 matched
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `fmt/time/time_crate.rs` vs expected `fmt/time/time_crate.rs`
- **Proposed provenance header:** `// port-lint: source fmt/time/time_crate.rs` (current: `// port-lint: source fmt/time/time_crate.rs`)
- **Lint issues:** 1

### 27. field.delimited

- **Target:** `field.Delimited [PROVENANCE-FALLBACK]`
- **Similarity:** 0.42
- **Dependents:** 0
- **Priority Score:** 51505.8
- **Functions:** 8/12 matched (target 9)
- **Missing functions:** `finish`, `writer`, `delimited_visitor`, `delimited_new_visitor`
- **Types:** 2/3 matched (target 2)
- **Missing types:** `Visitor`
- **Tests:** 0/2 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `field/delimited.rs` vs expected `field/delimited.rs`
- **Proposed provenance header:** `// port-lint: source field/delimited.rs` (current: `// port-lint: source field/delimited.rs`)
- **Lint issues:** 1

### 28. time.chrono_crate

- **Target:** `time.ChronoCrate [PROVENANCE-FALLBACK]`
- **Similarity:** 0.27
- **Dependents:** 0
- **Priority Score:** 51007.3
- **Functions:** 2/7 matched (target 6)
- **Missing functions:** `format_time`, `test_chrono_format_time_utc_default`, `test_chrono_format_time_utc_custom`, `test_chrono_format_time_local_default`, `test_chrono_format_time_local_custom`
- **Types:** 3/3 matched (target 5)
- **Missing types:** _none_
- **Tests:** 0/4 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `fmt/time/chrono_crate.rs` vs expected `fmt/time/chrono_crate.rs`
- **Proposed provenance header:** `// port-lint: source fmt/time/chrono_crate.rs` (current: `// port-lint: source fmt/time/chrono_crate.rs`)
- **Lint issues:** 1

### 29. field.display

- **Target:** `field.Display [PROVENANCE-FALLBACK]`
- **Similarity:** 0.40
- **Dependents:** 0
- **Priority Score:** 41206.0
- **Functions:** 7/10 matched (target 7)
- **Missing functions:** `new`, `finish`, `writer`
- **Types:** 1/2 matched
- **Missing types:** `Visitor`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `field/display.rs` vs expected `field/display.rs`
- **Proposed provenance header:** `// port-lint: source field/display.rs` (current: `// port-lint: source field/display.rs`)
- **Lint issues:** 1

### 30. tracing-subscriber.util

- **Target:** `tracingsubscriber.Util [PROVENANCE-FALLBACK]`
- **Similarity:** 0.16
- **Dependents:** 0
- **Priority Score:** 40808.4
- **Functions:** 2/6 matched (target 2)
- **Missing functions:** `set_default`, `new`, `fmt`, `source`
- **Types:** 2/2 matched
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `util.rs` vs expected `util.rs`
- **Proposed provenance header:** `// port-lint: source util.rs` (current: `// port-lint: source util.rs`)
- **Lint issues:** 1

### 31. field.debug

- **Target:** `field.Debug [PROVENANCE-FALLBACK]`
- **Similarity:** 0.49
- **Dependents:** 0
- **Priority Score:** 31205.1
- **Functions:** 8/10 matched (target 8)
- **Missing functions:** `finish`, `writer`
- **Types:** 1/2 matched
- **Missing types:** `Visitor`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `field/debug.rs` vs expected `field/debug.rs`
- **Proposed provenance header:** `// port-lint: source field/debug.rs` (current: `// port-lint: source field/debug.rs`)
- **Lint issues:** 1

### 32. tracing-subscriber.sync

- **Target:** `tracingsubscriber.Sync [PROVENANCE-FALLBACK]`
- **Similarity:** 0.11
- **Dependents:** 0
- **Priority Score:** 30708.9
- **Functions:** 3/6 matched (target 5)
- **Missing functions:** `new`, `try_read`, `default`
- **Types:** 1/1 matched (target 2)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `sync.rs` vs expected `sync.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:sync.rs` vs expected `sync.rs`
- **Proposed provenance header:** `// port-lint: source sync.rs` (current: `// port-lint: source sync.rs`)
- **Proposed provenance header:** `// port-lint: tests sync.rs` (current: `// port-lint: tests sync.rs`)
- **Lint issues:** 2

### 33. time.datetime

- **Target:** `time.Datetime [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 30410.0
- **Functions:** 0/3 matched (target 1)
- **Missing functions:** `fmt`, `from`, `test_datetime`
- **Types:** 1/1 matched (target 2)
- **Missing types:** _none_
- **Tests:** 0/1 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `fmt/time/datetime.rs` vs expected `fmt/time/datetime.rs`
- **Proposed provenance header:** `// port-lint: source fmt/time/datetime.rs` (current: `// port-lint: source fmt/time/datetime.rs`)
- **Lint issues:** 1

### 34. filter.mod

- **Target:** `filter.Mod [STUB] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 30310.0
- **Functions:** 0/3 matched (target 6)
- **Missing functions:** `is_plf_downcast_marker`, `subscriber_has_plf`, `layer_has_plf`
- **Types:** 0/0 matched (target 2)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `filter/mod.rs` vs expected `filter/mod.rs`
- **Proposed provenance header:** `// port-lint: source filter/mod.rs` (current: `// port-lint: source filter/mod.rs`)
- **Lint issues:** 1

### 35. registry.stack

- **Target:** `registry.Stack [PROVENANCE-FALLBACK]`
- **Similarity:** 0.39
- **Dependents:** 0
- **Priority Score:** 20806.1
- **Functions:** 4/6 matched (target 4)
- **Missing functions:** `pop_last_span`, `pop_first_span`
- **Types:** 2/2 matched
- **Missing types:** _none_
- **Tests:** 0/2 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `registry/stack.rs` vs expected `registry/stack.rs`
- **Proposed provenance header:** `// port-lint: source registry/stack.rs` (current: `// port-lint: source registry/stack.rs`)
- **Lint issues:** 1

### 36. time.mod

- **Target:** `time.Mod [STUB] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 810.0
- **Functions:** 5/5 matched (target 9)
- **Missing functions:** _none_
- **Types:** 3/3 matched
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `fmt/time/mod.rs` vs expected `fmt/time/mod.rs`
- **Proposed provenance header:** `// port-lint: source fmt/time/mod.rs` (current: `// port-lint: source fmt/time/mod.rs`)
- **Lint issues:** 1

## Success Criteria

For each file to be considered "complete":
- **Similarity ≥ 0.85** (Excellent threshold)
- All public APIs ported
- All tests ported
- Documentation ported
- port-lint header present

## Reexport / Wiring Modules

These files match `reexport_modules` patterns in `.ast_distance_config.json`. They are filtered out of
normal priority and missing-file ladders because they are wiring
modules, not direct logic ports. Consult them for call-site routing;
do not treat them as the next implementation target by default.

### Matched

| Source | Target | Path |
|--------|--------|------|
| `tracing-subscriber.lib` | `tracingsubscriber.Lib` | `tracing-subscriber/src/lib` |
| `tracing-subscriber.macros` | `tracingsubscriber.Macros` | `tracing-subscriber/src/macros` |
| `tracing-subscriber.prelude` | `tracingsubscriber.Prelude` | `tracing-subscriber/src/prelude` |


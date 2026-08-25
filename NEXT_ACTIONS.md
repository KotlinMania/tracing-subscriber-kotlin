# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Files Present:** 39/40 (97.5%)
- **Function parity:** 219/850 matched (target 327) — 25.8%
- **Class/type parity:** 56/197 matched (target 99) — 28.4%
- **Combined symbol parity:** 275/1047 matched (target 426) — 26.3%
- **Average inline-code cosine:** 0.20 (function body across 30 matched files)
- **Average documentation cosine:** 0.31 (doc text across 30 matched files)
- **Cheat-zeroed Files:** 12
- **Critical Issues:** 37 files with <0.60 function similarity

## Priority 1: Fix Incomplete High-Dependency Files

No incomplete high-dependency files detected.

## Priority 2: Port Missing High-Value Files

Critical missing files (>10 dependencies):

No missing high-value files detected.

## Detailed Work Items

Every matched file is listed below with function and type symbol parity.

### 1. fmt.writer

- **Target:** `fmt.Writer`
- **Similarity:** 0.02
- **Dependents:** 2
- **Priority Score:** 2384309.8
- **Functions:** 3/27 matched (target 6)
- **Missing functions:** `with_max_level`, `with_min_level`, `with_filter`, `and`, `make_writer`, `new`, `with_stderr`, `flush`, `fmt`, `write_vectored`, `write_all`, `write_fmt`, `none`, `some`, `from`, `test_writer`, `has_lines`, `custom_writer_closure`, `custom_writer_struct`, `custom_writer_mutex`, `combinators_level_filters`, `combinators_or_else`, `combinators_or_else_chain`, `combinators_and`
- **Types:** 2/16 matched (target 2)
- **Missing types:** `MakeWriter`, `BoxMakeWriter`, `EitherWriter`, `OptionalWriter`, `WithMaxLevel`, `WithMinLevel`, `WithFilter`, `OrElse`, `Tee`, `MutexGuardWriter`, `ArcWriter`, `WriteAdaptor`, `Writer`, `Boxed`
- **Tests:** 0/9 matched

### 2. format.escape

- **Target:** `format.Escape`
- **Similarity:** 0.00
- **Dependents:** 1
- **Priority Score:** 1030410.0
- **Functions:** 0/2 matched (target 1)
- **Missing functions:** `write_str`, `fmt`
- **Types:** 1/2 matched (target 1)
- **Missing types:** `EscapingWriter`

### 3. filter.level

- **Target:** `filter.Level`
- **Similarity:** 0.65
- **Dependents:** 1
- **Priority Score:** 1000303.5
- **Functions:** 3/3 matched (target 9)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 3)
- **Missing types:** _none_

### 4. format.mod

- **Target:** `format.Mod [STUB]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 839110.0
- **Functions:** 5/71 matched (target 5)
- **Missing functions:** `add_fields`, `json`, `debug_fn`, `new`, `with_ansi`, `by_ref`, `write_str`, `write_char`, `write_fmt`, `has_ansi_escapes`, `bold`, `dimmed`, `italic`, `fmt`, `default`, `compact`, `pretty`, `without_time`, `with_thread_ids`, `with_thread_names`, `with_file`, `with_line_number`, `with_source_location`, `format_timestamp`, `flatten_event`, `with_current_span`, `with_span_list`, `format_fields`, `make_visitor`, `maybe_pad`, `record_str`, `record_error`, `record_debug`, `finish`, `writer`, `paint`, `prefix`, `suffix`, `contains`, `with_kind`, `trace_new`, `trace_enter`, `trace_exit`, `trace_close`, `format_time`, `disable_everything`, `test_ansi`, `test_without_ansi`, `test_without_level`, `with_line_number_and_file_name`, `with_filename`, `pretty_default`, `assert_info_hello`, `assert_info_hello_ignore_numeric`, `test_overridden_parents`, `test_overridden_parents_in_scope`, `run_test`, `with_ansi_true`, `with_ansi_false`, `without_ansi`, `without_level`, `overridden_parents`, `overridden_parents_in_scope`, `format_nanos`, `fmt_span_combinations`, `current_path`
- **Types:** 3/20 matched (target 5)
- **Missing types:** `FormatEvent`, `FormatFields`, `Writer`, `FieldFn`, `FieldFnVisitor`, `DefaultFields`, `DefaultVisitor`, `Visitor`, `ErrorSourceList`, `FmtCtx`, `Style`, `FmtThreadName`, `FmtLevel`, `FmtSpan`, `FmtSpanConfig`, `TimingDisplay`, `MockTime`
- **Tests:** 0/22 matched

### 5. fmt.fmt_layer

- **Target:** `fmt.FmtLayer`
- **Similarity:** 0.04
- **Dependents:** 0
- **Priority Score:** 738009.6
- **Functions:** 6/71 matched (target 7)
- **Missing functions:** `new`, `map_event_format`, `writer`, `writer_mut`, `set_ansi`, `set_span_events`, `with_test_writer`, `log_internal_errors`, `map_writer`, `with_timer`, `without_time`, `with_span_events`, `with_file`, `with_line_number`, `with_thread_ids`, `with_thread_names`, `compact`, `pretty`, `json`, `flatten_event`, `with_current_span`, `with_span_list`, `fmt_fields`, `map_fmt_fields`, `default`, `make_ctx`, `as_writer`, `fmt`, `deref`, `on_new_span`, `on_record`, `on_enter`, `on_exit`, `on_close`, `downcast_raw`, `format_fields`, `visit_spans`, `metadata`, `span`, `exists`, `lookup_current`, `current_span`, `parent_span`, `span_scope`, `event_scope`, `field_format`, `impls`, `fmt_layer_downcasts`, `fmt_layer_downcasts_to_parts`, `is_lookup_span`, `assert_lookup_span`, `sanitize_timings`, `format_error_print_to_stderr`, `format_error_ignore_if_log_internal_errors_is_false`, `synthesize_span_none`, `synthesize_span_active`, `synthesize_span_close`, `synthesize_span_close_no_timing`, `synthesize_span_full`, `make_writer_based_on_meta`, `make_writer`, `make_writer_for`, `layer_no_color`, `drop`, `modify_span_events`
- **Types:** 1/9 matched (target 1)
- **Missing types:** `FormattedFields`, `Target`, `FmtContext`, `Timings`, `AlwaysError`, `MakeByTarget`, `Writer`, `RestoreEnvVar`
- **Tests:** 0/19 matched
- **Lint issues:** 1

### 6. fmt.mod

- **Target:** `fmt.Mod [STUB]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 617010.0
- **Functions:** 8/63 matched (target 9)
- **Missing functions:** `layer`, `builder`, `new`, `default`, `register_callsite`, `enabled`, `new_span`, `record`, `record_follows_from`, `event_enabled`, `event`, `enter`, `exit`, `current_span`, `clone_span`, `try_close`, `max_level_hint`, `downcast_raw`, `span_data`, `try_init`, `from`, `with_timer`, `without_time`, `with_span_events`, `log_internal_errors`, `with_file`, `with_line_number`, `with_thread_names`, `with_thread_ids`, `compact`, `pretty`, `json`, `flatten_event`, `with_current_span`, `with_span_list`, `with_filter_reloading`, `reload_handle`, `fmt_fields`, `with_env_filter`, `with_max_level`, `with_test_writer`, `map_event_format`, `map_fmt_fields`, `map_writer`, `map_error`, `buf`, `write`, `flush`, `get_string`, `make_writer`, `impls`, `subscriber_downcasts`, `subscriber_downcasts_to_parts`, `is_lookup_span`, `assert_lookup_span`
- **Types:** 1/7 matched (target 1)
- **Missing types:** `Subscriber`, `Formatter`, `Data`, `MockWriter`, `MockMakeWriter`, `Writer`
- **Tests:** 0/11 matched

### 7. registry.sharded

- **Target:** `registry.Sharded [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 585910.0
- **Functions:** 0/49 matched (target 0)
- **Missing functions:** `default`, `idx_to_id`, `id_to_idx`, `get`, `start_close`, `has_per_layer_filters`, `span_stack`, `register_callsite`, `enabled`, `new_span`, `record`, `record_follows_from`, `event_enabled`, `event`, `enter`, `exit`, `clone_span`, `current_span`, `try_close`, `span_data`, `register_filter`, `set_closing`, `drop`, `id`, `metadata`, `parent`, `extensions`, `extensions_mut`, `is_enabled_for`, `set_interest`, `clear`, `on_close`, `single_layer_can_access_closed_span`, `multiple_layers_can_access_closed_span`, `on_new_span`, `new`, `is_open`, `is_closed`, `assert_closed`, `assert_open`, `assert_removed`, `assert_not_removed`, `assert_last_closed`, `assert_closed_in_order`, `spans_are_removed_from_registry`, `spans_are_only_closed_when_the_last_ref_drops`, `span_enter_guards_are_dropped_out_of_order`, `child_closes_parent`, `child_closes_grandparent`
- **Types:** 1/10 matched (target 2)
- **Missing types:** `Registry`, `DataInner`, `CloseGuard`, `NullCallsite`, `AssertionLayer`, `CloseLayer`, `CloseHandle`, `CloseState`, `SetRemoved`
- **Tests:** 0/18 matched

### 8. env.mod

- **Target:** `env.Mod [STUB]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 414810.0
- **Functions:** 6/41 matched (target 9)
- **Missing functions:** `clone`, `from_env`, `try_new`, `try_from_default_env`, `try_from_env`, `on_new_span`, `on_enter`, `on_exit`, `on_close`, `on_record`, `cares_about_span`, `base_interest`, `register_callsite`, `from_str`, `from`, `default`, `fmt`, `source`, `new_span`, `event`, `record`, `record_follows_from`, `enter`, `exit`, `set_interest`, `metadata`, `callsite_enabled_no_span_directive`, `callsite_off`, `callsite_enabled_includes_span_directive`, `callsite_enabled_includes_span_directive_field`, `callsite_enabled_includes_span_directive_multiple_fields`, `roundtrip`, `size_of_filters`, `print_sz`, `parse_empty_string`
- **Types:** 1/7 matched (target 1)
- **Missing types:** `FieldMap`, `FromEnvError`, `ErrorKind`, `Err`, `NoSubscriber`, `Cs`
- **Tests:** 0/17 matched
- **Lint issues:** 1

### 9. format.json

- **Target:** `format.Json`
- **Similarity:** 0.01
- **Dependents:** 0
- **Priority Score:** 414209.9
- **Functions:** 1/36 matched (target 1)
- **Missing functions:** `flatten_event`, `with_current_span`, `with_span_list`, `serialize`, `default`, `new`, `format_fields`, `add_fields`, `fmt`, `writer`, `finish`, `record_value`, `record_f64`, `record_i64`, `record_u64`, `record_bool`, `record_str`, `record_bytes`, `record_debug`, `format_time`, `subscriber`, `json`, `json_filename`, `json_line_number`, `json_flattened_event`, `json_disabled_current_span_event`, `json_disabled_span_list_event`, `json_nested_span`, `json_no_span`, `record_works`, `json_span_event_show_correct_context`, `json_span_event_with_no_fields`, `parse_as_json`, `test_json`, `test_json_with_line_number`
- **Types:** 0/6 matched (target 1)
- **Missing types:** `Json`, `SerializableContext`, `SerializableSpan`, `JsonFields`, `JsonVisitor`, `MockTime`
- **Tests:** 0/16 matched

### 10. env.field

- **Target:** `env.Field`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 384110.0
- **Functions:** 0/30 matched (target 1)
- **Missing functions:** `eq`, `cmp`, `partial_cmp`, `has_value`, `name`, `parse`, `fmt`, `value_match_f64`, `parse_regex`, `parse_non_regex`, `from_str`, `as_ref`, `str_matches`, `debug_matches`, `into_debug_match`, `new`, `write_str`, `to_span_match`, `visitor`, `is_matched`, `is_matched_slow`, `filter`, `record_f64`, `record_i64`, `record_u64`, `record_bool`, `record_str`, `record_debug`, `debug_struct_match`, `debug_struct_not_match`
- **Types:** 3/11 matched (target 10)
- **Missing types:** `Match`, `SpanMatch`, `MatchVisitor`, `MatchPattern`, `MatchDebug`, `Err`, `Matcher`, `MyStruct`
- **Tests:** 0/2 matched

### 11. layer_filters.mod

- **Target:** `layerfilters.Mod [STUB]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 354910.0
- **Functions:** 12/41 matched (target 12)
- **Missing functions:** `new`, `and`, `or`, `not`, `boxed`, `callsite_enabled`, `id`, `did_enable`, `filter`, `filter_mut`, `inner`, `inner_mut`, `on_register_dispatch`, `on_layer`, `downcast_raw`, `fmt`, `disabled`, `none`, `is_disabled`, `set`, `is_enabled`, `any_enabled`, `add_interest`, `clear_enabled`, `take_interest`, `filter_map`, `is_plf_downcast_marker`, `subscriber_has_plf`, `layer_has_plf`
- **Types:** 2/8 matched (target 2)
- **Missing types:** `FilterMap`, `FilterState`, `DebugCounters`, `FilterExt`, `MagicPlfDowncastMarker`, `FmtBitset`

### 12. filter.directive

- **Target:** `filter.Directive`
- **Similarity:** 0.08
- **Dependents:** 0
- **Priority Score:** 283409.2
- **Functions:** 3/25 matched (target 4)
- **Missing functions:** `is_empty`, `default`, `directives`, `directives_for`, `into_vec`, `from_iter`, `extend`, `into_iter`, `enabled`, `target_enabled`, `directives_for_target`, `new`, `cares_about_target`, `cmp`, `partial_cmp`, `level`, `fmt`, `from_str`, `msg`, `description`, `source`, `from`
- **Types:** 3/9 matched (target 3)
- **Missing types:** `FilterVec`, `Match`, `ParseErrorKind`, `Item`, `IntoIter`, `Err`
- **Tests:** 0/1 matched

### 13. env.directive

- **Target:** `env.Directive`
- **Similarity:** 0.21
- **Dependents:** 0
- **Priority Score:** 265607.8
- **Functions:** 29/48 matched (target 30)
- **Missing functions:** `to_static`, `is_static`, `field_matcher`, `make_tables`, `deregexify`, `level`, `from_str`, `default`, `partial_cmp`, `cmp`, `fmt`, `from`, `matcher`, `has_value_filters`, `to_span_match`, `record_update`, `parse_directives`, `expect_parse`, `test_parse_bare_level`
- **Types:** 1/8 matched (target 2)
- **Missing types:** `Dynamics`, `Statics`, `CallsiteMatcher`, `SpanMatcher`, `MatchSet`, `ParseState`, `Err`
- **Tests:** 24/27 matched

### 14. filter.targets

- **Target:** `filter.Targets`
- **Similarity:** 0.18
- **Dependents:** 0
- **Priority Score:** 234108.2
- **Functions:** 17/36 matched (target 31)
- **Missing functions:** `new`, `iter`, `interested`, `extend`, `from_iter`, `from_str`, `into_iter`, `fmt`, `next`, `size_hint`, `expect_parse`, `expect_parse_ralith`, `expect_parse_level_directives`, `parse_ralith`, `parse_ralith_uc`, `parse_ralith_mixed`, `expect_parse_valid`, `print_sz`, `test_roundtrip`
- **Types:** 1/5 matched (target 2)
- **Missing types:** `Err`, `Item`, `IntoIter`, `Iter`
- **Tests:** 8/17 matched

### 15. format.pretty

- **Target:** `format.Pretty`
- **Similarity:** 0.02
- **Dependents:** 0
- **Priority Score:** 202109.8
- **Functions:** 1/17 matched (target 1)
- **Missing functions:** `default`, `style_for`, `with_source_location`, `format_fields`, `add_fields`, `new`, `with_ansi`, `make_visitor`, `with_style`, `write_padded`, `bold`, `record_str`, `record_error`, `record_debug`, `finish`, `writer`
- **Types:** 0/4 matched (target 1)
- **Missing types:** `Pretty`, `PrettyVisitor`, `PrettyFields`, `Visitor`

### 16. registry.extensions

- **Target:** `registry.Extensions`
- **Similarity:** 0.13
- **Dependents:** 0
- **Priority Score:** 172208.7
- **Functions:** 4/14 matched (target 9)
- **Missing functions:** `write`, `write_u64`, `finish`, `new`, `replace`, `get_mut`, `fmt`, `test_extensions`, `clear_retains_capacity`, `clear_drops_elements`
- **Types:** 1/8 matched (target 1)
- **Missing types:** `AnyMap`, `IdHasher`, `ExtensionsMut`, `ExtensionsInner`, `MyType`, `DropMePlease`, `DropMeTooPlease`
- **Tests:** 0/3 matched

### 17. field.mod

- **Target:** `field.Mod [STUB]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 162810.0
- **Functions:** 6/14 matched (target 11)
- **Missing functions:** `record`, `make_visitor`, `with`, `set_interest`, `metadata`, `new`, `finish`, `writer`
- **Types:** 6/14 matched (target 7)
- **Missing types:** `Visitor`, `MakeExtMarker`, `RecordFieldsMarker`, `TestAttrs1`, `TestAttrs2`, `TestCallsite1`, `MakeDebug`, `DebugVisitor`
- **Lint issues:** 10

### 18. registry.mod

- **Target:** `registry.Mod [STUB]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 152310.0
- **Functions:** 4/17 matched (target 26)
- **Missing functions:** `next`, `id`, `metadata`, `name`, `fields`, `parent`, `extensions`, `extensions_mut`, `try_with_filter`, `with_filter`, `spanref_scope_iteration_order`, `on_enter`, `spanref_scope_fromroot_iteration_order`
- **Types:** 4/6 matched (target 9)
- **Missing types:** `Item`, `PrintingLayer`
- **Tests:** 0/2 matched
- **Lint issues:** 3

### 19. reload

- **Target:** `tracingsubscriber.Reload`
- **Similarity:** 0.30
- **Dependents:** 0
- **Priority Score:** 143107.0
- **Functions:** 16/27 matched (target 16)
- **Missing functions:** `on_register_dispatch`, `on_layer`, `downcast_raw`, `callsite_enabled`, `handle`, `clone_current`, `clone`, `poisoned`, `is_poisoned`, `is_dropped`, `fmt`
- **Types:** 1/4 matched (target 2)
- **Missing types:** `Layer`, `Error`, `ErrorKind`

### 20. layer.layered

- **Target:** `layer.Layered`
- **Similarity:** 0.41
- **Dependents:** 0
- **Priority Score:** 123605.9
- **Functions:** 23/34 matched (target 29)
- **Missing functions:** `is`, `downcast_ref`, `on_register_dispatch`, `drop_span`, `downcast_raw`, `on_layer`, `ctx`, `new`, `pick_interest`, `pick_level_hint`, `fmt`
- **Types:** 1/2 matched (target 4)
- **Missing types:** `Data`

### 21. filter.filter_fn

- **Target:** `filter.FilterFn`
- **Similarity:** 0.12
- **Dependents:** 0
- **Priority Score:** 111708.8
- **Functions:** 4/15 matched (target 12)
- **Missing functions:** `new`, `with_max_level_hint`, `is_enabled`, `is_callsite_enabled`, `is_below_max_level`, `register_callsite`, `from`, `fmt`, `with_callsite_filter`, `default_callsite_enabled`, `clone`
- **Types:** 2/2 matched (target 3)
- **Missing types:** _none_

### 22. layer.mod

- **Target:** `layer.Mod [STUB]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 92710.0
- **Functions:** 16/23 matched (target 16)
- **Missing functions:** `on_register_dispatch`, `on_layer`, `boxed`, `downcast_raw`, `layer_is_none`, `subscriber_is_none`, `new`
- **Types:** 2/4 matched (target 2)
- **Missing types:** `Identity`, `NoneLayerMarker`
- **Lint issues:** 20

### 23. layer.context

- **Target:** `layer.Context`
- **Similarity:** 0.35
- **Dependents:** 0
- **Priority Score:** 91906.5
- **Functions:** 9/18 matched (target 10)
- **Missing functions:** `new`, `span`, `lookup_current_filtered`, `with_filter`, `is_enabled_for`, `if_enabled_for`, `is_enabled_inner`, `none`, `clone`
- **Types:** 1/1 matched
- **Missing types:** _none_

### 24. time.chrono_crate

- **Target:** `time.ChronoCrate`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 91010.0
- **Functions:** 0/7 matched (target 2)
- **Missing functions:** `rfc_3339`, `new`, `format_time`, `test_chrono_format_time_utc_default`, `test_chrono_format_time_utc_custom`, `test_chrono_format_time_local_default`, `test_chrono_format_time_local_custom`
- **Types:** 1/3 matched (target 1)
- **Missing types:** `ChronoLocal`, `ChronoFmtType`
- **Tests:** 0/4 matched

### 25. layer_filters.combinator

- **Target:** `layerfilters.Combinator`
- **Similarity:** 0.19
- **Dependents:** 0
- **Priority Score:** 81508.1
- **Functions:** 4/12 matched
- **Missing functions:** `new`, `on_new_span`, `on_record`, `on_enter`, `on_exit`, `on_close`, `clone`, `fmt`
- **Types:** 3/3 matched
- **Missing types:** _none_

### 26. env.builder

- **Target:** `env.Builder`
- **Similarity:** 0.21
- **Dependents:** 0
- **Priority Score:** 71207.9
- **Functions:** 4/11 matched (target 6)
- **Missing functions:** `with_env_var`, `from_env_lossy`, `from_env`, `try_from_env`, `from_directives`, `env_var_name`, `default`
- **Types:** 1/1 matched
- **Missing types:** _none_

### 27. time.time_crate

- **Target:** `time.TimeCrate`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 70910.0
- **Functions:** 0/6 matched (target 2)
- **Missing functions:** `rfc_3339`, `new`, `format_time`, `default`, `local_rfc_3339`, `format_datetime`
- **Types:** 2/3 matched (target 2)
- **Missing types:** `OffsetTime`

### 28. field.delimited

- **Target:** `field.Delimited`
- **Similarity:** 0.42
- **Dependents:** 0
- **Priority Score:** 51505.8
- **Functions:** 8/12 matched (target 9)
- **Missing functions:** `finish`, `writer`, `delimited_visitor`, `delimited_new_visitor`
- **Types:** 2/3 matched (target 2)
- **Missing types:** `Visitor`
- **Tests:** 0/2 matched

### 29. field.display

- **Target:** `field.Display`
- **Similarity:** 0.40
- **Dependents:** 0
- **Priority Score:** 41206.0
- **Functions:** 7/10 matched (target 7)
- **Missing functions:** `new`, `finish`, `writer`
- **Types:** 1/2 matched
- **Missing types:** `Visitor`

### 30. util

- **Target:** `tracingsubscriber.Util`
- **Similarity:** 0.16
- **Dependents:** 0
- **Priority Score:** 40808.4
- **Functions:** 2/6 matched (target 2)
- **Missing functions:** `set_default`, `new`, `fmt`, `source`
- **Types:** 2/2 matched
- **Missing types:** _none_

### 31. time.datetime

- **Target:** `time.Datetime`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 40410.0
- **Functions:** 0/3 matched (target 1)
- **Missing functions:** `fmt`, `from`, `test_datetime`
- **Types:** 0/1 matched
- **Missing types:** `DateTime`
- **Tests:** 0/1 matched

### 32. field.debug

- **Target:** `field.Debug`
- **Similarity:** 0.49
- **Dependents:** 0
- **Priority Score:** 31205.1
- **Functions:** 8/10 matched (target 8)
- **Missing functions:** `finish`, `writer`
- **Types:** 1/2 matched
- **Missing types:** `Visitor`

### 33. time.mod

- **Target:** `time.Mod [STUB]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 30810.0
- **Functions:** 3/5 matched (target 4)
- **Missing functions:** `default`, `from`
- **Types:** 2/3 matched (target 2)
- **Missing types:** `FormatTime`

### 34. sync

- **Target:** `tracingsubscriber.Sync`
- **Similarity:** 0.11
- **Dependents:** 0
- **Priority Score:** 30708.9
- **Functions:** 3/6 matched (target 5)
- **Missing functions:** `new`, `try_read`, `default`
- **Types:** 1/1 matched (target 2)
- **Missing types:** _none_

### 35. filter.mod

- **Target:** `filter.Mod [STUB]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 30310.0
- **Functions:** 0/3 matched (target 6)
- **Missing functions:** `is_plf_downcast_marker`, `subscriber_has_plf`, `layer_has_plf`
- **Types:** 0/0 matched (target 2)
- **Missing types:** _none_

### 36. registry.stack

- **Target:** `registry.Stack`
- **Similarity:** 0.39
- **Dependents:** 0
- **Priority Score:** 20806.1
- **Functions:** 4/6 matched (target 4)
- **Missing functions:** `pop_last_span`, `pop_first_span`
- **Types:** 2/2 matched
- **Missing types:** _none_
- **Tests:** 0/2 matched

### 37. lib

- **Target:** `tracingsubscriber.Lib [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10110.0
- **Functions:** 0/0 matched (target 3)
- **Missing functions:** _none_
- **Types:** 0/1 matched (target 3)
- **Missing types:** `Sealed`

### 38. macros

- **Target:** `tracingsubscriber.Macros [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10.0
- **Functions:** 0/0 matched (target 1)
- **Missing functions:** _none_
- **Types:** 0/0 matched
- **Missing types:** _none_

### 39. prelude

- **Target:** `tracingsubscriber.Prelude`
- **Similarity:** 1.00
- **Dependents:** 0
- **Priority Score:** 0.0
- **Functions:** 0/0 matched
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 6)
- **Missing types:** _none_

## Success Criteria

For each file to be considered "complete":
- **Similarity ≥ 0.85** (Excellent threshold)
- All public APIs ported
- All tests ported
- Documentation ported
- port-lint header present


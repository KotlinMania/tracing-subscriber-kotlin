# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Files Present:** 0/40 (0.0%)
- **Function parity:** 0/724 matched — 0.0%
- **Class/type parity:** 0/127 matched — 0.0%
- **Combined symbol parity:** 0/851 matched — 0.0%
- **Average inline-code cosine:** 0.00 (function body across 0 matched files)
- **Average documentation cosine:** 0.00 (doc text across 0 matched files)
- **Cheat-zeroed Files:** 0
- **Critical Issues:** 0 files with <0.60 function similarity

## Priority 1: Fix Incomplete High-Dependency Files

No incomplete high-dependency files detected.

## Priority 2: Port Missing High-Value Files

Critical missing files (>10 dependencies):

No missing high-value files detected.

## Detailed Work Items

Every matched file is listed below with function and type symbol parity.

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

### Missing

| Source | Expected target | Deps | Source path | Expected path |
|--------|-----------------|------|-------------|---------------|
| `field.mod` | `field.Mod` | 0 | `field/mod.rs` | `field/Mod.kt` |
| `env.mod` | `filter.env.Mod` | 0 | `filter/env/mod.rs` | `filter/env/Mod.kt` |
| `layer_filters.mod` | `filter.layerfilters.Mod` | 0 | `filter/layer_filters/mod.rs` | `filter/layerfilters/Mod.kt` |
| `filter.mod` | `filter.Mod` | 0 | `filter/mod.rs` | `filter/Mod.kt` |
| `format.mod` | `fmt.format.Mod` | 0 | `fmt/format/mod.rs` | `fmt/format/Mod.kt` |
| `fmt.mod` | `fmt.Mod` | 0 | `fmt/mod.rs` | `fmt/Mod.kt` |
| `time.mod` | `fmt.time.Mod` | 0 | `fmt/time/mod.rs` | `fmt/time/Mod.kt` |
| `layer.mod` | `layer.Mod` | 0 | `layer/mod.rs` | `layer/Mod.kt` |
| `lib` | `Lib` | 0 | `lib.rs` | `Lib.kt` |
| `registry.mod` | `registry.Mod` | 0 | `registry/mod.rs` | `registry/Mod.kt` |


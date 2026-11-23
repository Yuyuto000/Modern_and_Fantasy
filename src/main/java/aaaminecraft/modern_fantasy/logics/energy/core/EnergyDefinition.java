package aaaminecraft.modern_fantasy.logics.energy.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * <h1>EnergyDefinitionクラス</h1>
 *
 * エネルギーの種類を定義する基底クラス
 *
 * <p>
 *     科学エネルギーや魔術エネルギーなど、
 *     各エネルギーが持つ共通の定義情報を管理する。
 * </p>
 *
 * <p>
 *     このクラスはエネルギーの量や現在の状態を保持しない。
 *     あくまでエネルギーの種類と、その固有特性を定義する。
 * </p>
 */
public abstract class EnergyDefinition {

    private final String name;
    private final EnergyUnit unit;

    private final Map<EnergyPropertyType<?>, EnergyProperty<?>> properties = new HashMap<>();

    /**
     * EnergyDefinitionを生成する。
     *
     * @param name エネルギーの識別名称
     *             nullまたは空渡し不可
     * @param unit エネルギー量を表現する単位。
     *             null不可。
     * @throws IllegalArgumentException 各引数が不正な場合。不正条件は各パラメータ説明参照。エラーコード:[MF-ENERGY-007], [MF-ENERGY-019]
     */
    protected EnergyDefinition(String name, EnergyUnit unit) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("[MF-ENERGY-007] Energy name cannot be null or blank");
        if (unit == null) throw new IllegalArgumentException("[MF-ENERGY-019] Energy unit cannot be null");

        this.name = name;
        this.unit = unit;
    }

    /**
     * エネルギーに固有特性を登録する。
     *
     * <p>
     *     登録済みの特性と同じ識別型は二重登録できない。
     * </p>
     *
     * @param property 登録するエネルギー特性
     * @param <T> 特性値の型
     *
     * @throws IllegalArgumentException propertyがnullの場合、または同じ特性が既に登録されている場合
     */
    protected final <T> void registerProperty(EnergyProperty<T> property){

        if (property == null) throw new IllegalArgumentException("[MF-ENERGY-013] Property cannot be null");
        if (properties.putIfAbsent(property.type(), property) != null) throw new IllegalArgumentException("[MF-ENERGY-014] Property type is already registered: " + property.type().id());
    }

    /**
     * 登録されている特性の値を取得する。
     *
     * @param type 取得する特性の種類
     * @param <T> 特性値の値
     * @return 特性値。登録されていない場合は空のOptional。
     * @throws IllegalArgumentException typeがnullの場合。エラーコード:[MF-ENERGY-015]
     */
    public final <T> Optional<T> getPropertyValue(EnergyPropertyType<T> type){
        if (type == null) throw new IllegalArgumentException("[MF-ENERGY-015] Property type cannot be null");

        EnergyProperty<?> property = properties.get(type);
        if (property == null) return Optional.empty();

        return Optional.of(type.valueClass().cast(property.value()));
    }

    /**
     * 登録されているすべての特性を取得する。
     *
     * <p>
     *     返されるMapは変更できない独立したコピーである。
     * </p>
     *
     * @return 登録済み特性の不変Map
     */
    public final Map<EnergyPropertyType<?>, EnergyProperty<?>> getProperties() {
        return Map.copyOf(properties);
    }

    /**
     * エネルギー名称を取得する。
     *
     * @return エネルギー名称
     */
    public final String getName(){
        return name;
    }

    public final EnergyUnit getUnit(){
        return unit;
    }
}

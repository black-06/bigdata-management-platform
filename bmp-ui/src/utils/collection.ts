declare interface Array<T> {
    ToMap<K, V>(mapFn: (entity: T) => ({ key: K, value: V })): Map<K, V>;

    Add(value: T): Array<T>;
}

Array.prototype.ToMap = function <T, K, V>(mapFn: (entity: T) => ({ key: K, value: V })): Map<K, V> {
    let map = new Map<K, V>;
    this.forEach(value => {
        let entity = mapFn(value);
        map.set(entity.key, entity.value)
    })
    return map
}

Array.prototype.Add = function <T>(value: T): Array<T> {
    this.push(value)
    return this
}

declare interface Map<K, V> {
    ValueMap<T>(mapFn: (K: K, V: V) => T): T[];
}

Map.prototype.ValueMap = function <K, V, T>(mapFn: (key: K, value: V) => T): T[] {
    let array = new Array<T>();
    this.forEach((value, key) => {
        array.push(mapFn(key, value))
    })
    return array
}
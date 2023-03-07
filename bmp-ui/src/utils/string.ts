export function Trunc(s: string, length: number): string {
    if (s.length > length) {
        return s.substring(0, length) + "..."
    }
    return s
}
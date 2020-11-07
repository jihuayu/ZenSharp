package zensharp.value.builtin.expansion;

import zensharp.value.IAny;

/**
 * @author Stan
 */
public interface IByteExpansion {

    IAny member(byte value, String member);
}

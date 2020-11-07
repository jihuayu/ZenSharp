package zensharp.type.casting;

import zensharp.type.ZenType;

/**
 * @author Stan
 */
public interface ICastingRuleDelegate {
    
    void registerCastingRule(ZenType type, ICastingRule rule);
}

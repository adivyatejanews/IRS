/**
 * Generated by Gas3 v2.1.0 (Granite Data Services).
 *
 * WARNING: DO NOT CHANGE THIS FILE. IT MAY BE OVERWRITTEN EACH TIME YOU USE
 * THE GENERATOR. INSTEAD, EDIT THE INHERITED CLASS (SystemAccountRole.as).
 */

package org.hil.core.model {

    import flash.utils.IDataInput;
    import flash.utils.IDataOutput;
    import org.granite.meta;
    import org.granite.tide.IEntityManager;
    import org.granite.tide.IPropertyHolder;

    use namespace meta;

    [Managed]
    public class SystemAccountRoleBase extends AbstractEntity {

        private var _description:String;
        private var _systemAccount:SystemAccount;
        private var _systemRole:SystemRole;

        public function set description(value:String):void {
            _description = value;
        }
        public function get description():String {
            return _description;
        }

        public function set systemAccount(value:SystemAccount):void {
            _systemAccount = value;
        }
        public function get systemAccount():SystemAccount {
            return _systemAccount;
        }

        public function set systemRole(value:SystemRole):void {
            _systemRole = value;
        }
        public function get systemRole():SystemRole {
            return _systemRole;
        }

        override meta function merge(em:IEntityManager, obj:*):void {
            var src:SystemAccountRoleBase = SystemAccountRoleBase(obj);
            super.meta::merge(em, obj);
            if (meta::isInitialized()) {
               em.meta_mergeExternal(src._description, _description, null, this, 'description', function setter(o:*):void{_description = o as String}, false);
               em.meta_mergeExternal(src._systemAccount, _systemAccount, null, this, 'systemAccount', function setter(o:*):void{_systemAccount = o as SystemAccount}, false);
               em.meta_mergeExternal(src._systemRole, _systemRole, null, this, 'systemRole', function setter(o:*):void{_systemRole = o as SystemRole}, false);
            }
        }

        override public function readExternal(input:IDataInput):void {
            super.readExternal(input);
            if (meta::isInitialized()) {
                _description = input.readObject() as String;
                _systemAccount = input.readObject() as SystemAccount;
                _systemRole = input.readObject() as SystemRole;
            }
        }

        override public function writeExternal(output:IDataOutput):void {
            super.writeExternal(output);
            if (meta::isInitialized()) {
                output.writeObject((_description is IPropertyHolder) ? IPropertyHolder(_description).object : _description);
                output.writeObject((_systemAccount is IPropertyHolder) ? IPropertyHolder(_systemAccount).object : _systemAccount);
                output.writeObject((_systemRole is IPropertyHolder) ? IPropertyHolder(_systemRole).object : _systemRole);
            }
        }
    }
}

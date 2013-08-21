/**
 * Generated by Gas3 v2.1.0 (Granite Data Services).
 *
 * WARNING: DO NOT CHANGE THIS FILE. IT MAY BE OVERWRITTEN EACH TIME YOU USE
 * THE GENERATOR. INSTEAD, EDIT THE INHERITED CLASS (ChildrenUnder1.as).
 */

package org.hil.core.model {

    import flash.utils.IDataInput;
    import flash.utils.IDataOutput;
    import org.granite.meta;
    import org.granite.tide.IEntityManager;
    import org.granite.tide.IPropertyHolder;

    use namespace meta;

    [Managed]
    public class ChildrenUnder1Base extends AbstractEntity {

        private var _commune:Commune;
        private var _notes:String;
        private var _time:Date;
        private var _totalChildrenUnder1:Number;

        public function set commune(value:Commune):void {
            _commune = value;
        }
        public function get commune():Commune {
            return _commune;
        }

        public function set notes(value:String):void {
            _notes = value;
        }
        public function get notes():String {
            return _notes;
        }

        public function set time(value:Date):void {
            _time = value;
        }
        public function get time():Date {
            return _time;
        }

        public function set totalChildrenUnder1(value:Number):void {
            _totalChildrenUnder1 = value;
        }
        public function get totalChildrenUnder1():Number {
            return _totalChildrenUnder1;
        }

        override meta function merge(em:IEntityManager, obj:*):void {
            var src:ChildrenUnder1Base = ChildrenUnder1Base(obj);
            super.meta::merge(em, obj);
            if (meta::isInitialized()) {
               em.meta_mergeExternal(src._commune, _commune, null, this, 'commune', function setter(o:*):void{_commune = o as Commune}, false);
               em.meta_mergeExternal(src._notes, _notes, null, this, 'notes', function setter(o:*):void{_notes = o as String}, false);
               em.meta_mergeExternal(src._time, _time, null, this, 'time', function setter(o:*):void{_time = o as Date}, false);
               em.meta_mergeExternal(src._totalChildrenUnder1, _totalChildrenUnder1, null, this, 'totalChildrenUnder1', function setter(o:*):void{_totalChildrenUnder1 = o as Number}, false);
            }
        }

        override public function readExternal(input:IDataInput):void {
            super.readExternal(input);
            if (meta::isInitialized()) {
                _commune = input.readObject() as Commune;
                _notes = input.readObject() as String;
                _time = input.readObject() as Date;
                _totalChildrenUnder1 = function(o:*):Number { return (o is Number ? o as Number : Number.NaN) } (input.readObject());
            }
        }

        override public function writeExternal(output:IDataOutput):void {
            super.writeExternal(output);
            if (meta::isInitialized()) {
                output.writeObject((_commune is IPropertyHolder) ? IPropertyHolder(_commune).object : _commune);
                output.writeObject((_notes is IPropertyHolder) ? IPropertyHolder(_notes).object : _notes);
                output.writeObject((_time is IPropertyHolder) ? IPropertyHolder(_time).object : _time);
                output.writeObject((_totalChildrenUnder1 is IPropertyHolder) ? IPropertyHolder(_totalChildrenUnder1).object : _totalChildrenUnder1);
            }
        }
    }
}

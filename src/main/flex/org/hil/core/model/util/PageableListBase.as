/**
 * Generated by Gas3 v2.1.0 (Granite Data Services).
 *
 * WARNING: DO NOT CHANGE THIS FILE. IT MAY BE OVERWRITTEN EACH TIME YOU USE
 * THE GENERATOR. INSTEAD, EDIT THE INHERITED CLASS (PageableList.as).
 */

package org.hil.core.model.util {

    import flash.utils.IDataInput;
    import flash.utils.IDataOutput;
    import flash.utils.IExternalizable;
    import mx.collections.ListCollectionView;

    [Bindable]
    public class PageableListBase implements IExternalizable {

        private var _desc:Boolean;
        private var _firstResult:Number;
        private var _maxResults:Number;
        private var _orderedField:String;
        private var _resultCount:Number;
        private var _resultList:ListCollectionView;

        public function set desc(value:Boolean):void {
            _desc = value;
        }
        public function get desc():Boolean {
            return _desc;
        }

        public function set firstResult(value:Number):void {
            _firstResult = value;
        }
        public function get firstResult():Number {
            return _firstResult;
        }

        public function set maxResults(value:Number):void {
            _maxResults = value;
        }
        public function get maxResults():Number {
            return _maxResults;
        }

        public function set orderedField(value:String):void {
            _orderedField = value;
        }
        public function get orderedField():String {
            return _orderedField;
        }

        public function set resultCount(value:Number):void {
            _resultCount = value;
        }
        public function get resultCount():Number {
            return _resultCount;
        }

        public function set resultList(value:ListCollectionView):void {
            _resultList = value;
        }
        public function get resultList():ListCollectionView {
            return _resultList;
        }

        public function readExternal(input:IDataInput):void {
            _desc = input.readObject() as Boolean;
            _firstResult = function(o:*):Number { return (o is Number ? o as Number : Number.NaN) } (input.readObject());
            _maxResults = function(o:*):Number { return (o is Number ? o as Number : Number.NaN) } (input.readObject());
            _orderedField = input.readObject() as String;
            _resultCount = function(o:*):Number { return (o is Number ? o as Number : Number.NaN) } (input.readObject());
            _resultList = input.readObject() as ListCollectionView;
        }

        public function writeExternal(output:IDataOutput):void {
            output.writeObject(_desc);
            output.writeObject(_firstResult);
            output.writeObject(_maxResults);
            output.writeObject(_orderedField);
            output.writeObject(_resultCount);
            output.writeObject(_resultList);
        }
    }
}
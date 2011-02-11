#!/usr/bin/python
#  Licensed to the Apache Software Foundation (ASF) under one
#  or more contributor license agreements.  See the NOTICE file
#  distributed with this work for additional information
#  regarding copyright ownership.  The ASF licenses this file
#  to you under the Apache License, Version 2.0 (the
#  "License"); you may not use this file except in compliance
#  with the License.  You may obtain a copy of the License at
#  
#    http://www.apache.org/licenses/LICENSE-2.0
#    
#  Unless required by applicable law or agreed to in writing,
#  software distributed under the License is distributed on an
#  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
#  KIND, either express or implied.  See the License for the
#  specific language governing permissions and limitations
#  under the License.

# Test the simple parallel programming components

import unittest
from property import *
from reference import *
from environment import *

import true_
import false_
import number
import text
import name
import nothing
import pair
import if_
import and_
import or_
import not_
import equals
import lesser
import greater
import list_
import empty
import first
import rest
import foreach
import filter_

def testValues():
    assert true_.get(()) == True
    assert false_.get(()) == False
    assert nothing.get(()) == None
    assert number.get((), mkprop('value', lambda: 1)) == 1
    assert text.get((), mkprop('value', lambda: 'abc')) == 'abc'
    assert name.get((), mkprop('value', lambda: 'abc')) == "'abc"
    assert pair.get((), mkref('a', lambda id: 'abc'), mkref('b', lambda id: 'def')) == ('abc', 'def')

    assert if_.get((), mkref('cond', lambda id: True), mkref('then', lambda id: 'abc'), mkref('els', lambda id: 'def')) == 'abc'
    assert if_.get((), mkref('cond', lambda id: False), mkref('then', lambda id: 'abc'), mkref('els', lambda id: 'def')) == 'def'
    assert and_.get((), mkref('a', lambda id: False), mkref('b', lambda id: True)) == False
    assert and_.get((), mkref('a', lambda id: True), mkref('b', lambda id: True)) == True
    assert or_.get((), mkref('a', lambda id: False), mkref('b', lambda id: False)) == False
    assert or_.get((), mkref('a', lambda id: False), mkref('b', lambda id: True)) == True
    assert not_.get((), mkref('expr', lambda id: False)) == True
    assert not_.get((), mkref('expr', lambda id: True)) == False
    assert equals.get((), mkref('a', lambda id: 'abc'), mkref('b', lambda id: 'abc')) == True
    assert equals.get((), mkref('a', lambda id: 'abc'), mkref('b', lambda id: 'def')) == False
    assert lesser.get((), mkref('a', lambda id: 'abc'), mkref('b', lambda id: 'abc')) == False
    assert lesser.get((), mkref('a', lambda id: 'abc'), mkref('b', lambda id: 'def')) == True
    assert greater.get((), mkref('a', lambda id: 'abc'), mkref('b', lambda id: 'abc')) == False
    assert greater.get((), mkref('a', lambda id: 'def'), mkref('b', lambda id: 'abc')) == True

    assert list_.get((), mkref('first', lambda id: 'abc'), mkref('rest', lambda id: None)) == ('abc',)
    assert list_.get((), mkref('first', lambda id: 'abc'), mkref('rest', lambda id: ())) == ('abc',)
    assert list_.get((), mkref('first', lambda id: 'abc'), mkref('rest', lambda id: ('def',))) == ('abc','def')
    assert list_.get((), mkref('first', lambda id: 'abc'), mkref('rest', lambda id: ('def', 'ghi'))) == ('abc','def', 'ghi')
    assert empty.get(()) == ()
    assert first.get((), mkref('l', lambda id: ('abc', 'def'))) == 'abc'
    assert rest.get((), mkref('l', lambda id: ('abc', 'def', 'ghi'))) == ('def', 'ghi')
    assert rest.get((), mkref('l', lambda id: ('abc',))) == ()

    foreachenv = mkenv('foreach')
    assert foreach.get((), foreachenv, mkref('transform', lambda id: foreachenv.get(()) * 2), mkref('list', lambda id: (1, 2, 3))) == (2, 4, 6)

    filterenv = mkenv('filter')
    assert filter_.get((), filterenv, mkref('transform', lambda id: filterenv.get(()) % 2 == 0), mkref('list', lambda id: (1, 2, 3, 4))) == (2, 4)
    return True

if __name__ == '__main__':
    print 'Testing...'
    testValues()
    print 'OK'


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

from nuvem import true_
from nuvem import false_
from nuvem import number
from nuvem import text
from nuvem import name
from nuvem import nothing
from nuvem import assoc
from nuvem import if_
from nuvem import and_
from nuvem import or_
from nuvem import not_
from nuvem import equals
from nuvem import lesser
from nuvem import greater
from nuvem import cons
from nuvem import list_
from nuvem import empty
from nuvem import first
from nuvem import rest
from nuvem import append
from nuvem import itemnb
from nuvem import find
from nuvem import reverse
from nuvem import range_
from nuvem import map_
from nuvem import valueof
from nuvem import filter_
from nuvem import reduce_
from nuvem import add
from nuvem import subtract
from nuvem import multiply
from nuvem import divide
from nuvem import host
from nuvem import path
from nuvem import params
from nuvem import user
from nuvem import realm
from nuvem import email
from nuvem import contains
from nuvem import split
from nuvem import join
from nuvem import replace
from nuvem import lowercase
from nuvem import uppercase

def testValues():
    assert true_.get(()) == True
    assert false_.get(()) == False
    assert nothing.get(()) == None
    assert number.get((), mkprop('value', lambda: 1)) == 1
    assert text.get((), mkprop('value', lambda: 'abc')) == 'abc'
    assert name.get((), mkprop('value', lambda: 'abc')) == "'abc"
    assert assoc.get((), mkref('a', lambda r: 'abc'), mkref('b', lambda r: 'def')) == ('abc', 'def')
    return True

def testLogic():
    assert if_.get((), mkref('cond', lambda r: True), mkref('then', lambda r: 'abc'), mkref('els', lambda r: 'def')) == 'abc'
    assert if_.get((), mkref('cond', lambda r: False), mkref('then', lambda r: 'abc'), mkref('els', lambda r: 'def')) == 'def'
    assert and_.get((), mkref('a', lambda r: False), mkref('b', lambda r: True)) == False
    assert and_.get((), mkref('a', lambda r: True), mkref('b', lambda r: True)) == True
    assert or_.get((), mkref('a', lambda r: False), mkref('b', lambda r: False)) == False
    assert or_.get((), mkref('a', lambda r: False), mkref('b', lambda r: True)) == True
    assert not_.get((), mkref('expr', lambda r: False)) == True
    assert not_.get((), mkref('expr', lambda r: True)) == False
    assert equals.get((), mkref('a', lambda r: 'abc'), mkref('b', lambda r: 'abc')) == True
    assert equals.get((), mkref('a', lambda r: 'abc'), mkref('b', lambda r: 'def')) == False
    assert lesser.get((), mkref('a', lambda r: 'abc'), mkref('b', lambda r: 'abc')) == False
    assert lesser.get((), mkref('a', lambda r: 'abc'), mkref('b', lambda r: 'def')) == True
    assert greater.get((), mkref('a', lambda r: 'abc'), mkref('b', lambda r: 'abc')) == False
    assert greater.get((), mkref('a', lambda r: 'def'), mkref('b', lambda r: 'abc')) == True
    return True

def testLists():
    assert list_.get((), mkref('item', lambda r: None)) == ()
    assert list_.get((), mkref('item', lambda r: 'abc'), mkref('item', lambda r: None)) == ('abc',)
    assert list_.get((), mkref('item', lambda r: 'abc'), mkref('item', lambda r: 'def'), mkref('item', lambda r: None)) == ('abc', 'def')
    assert cons.get((), mkref('first', lambda r: 'abc'), mkref('rest', lambda r: None)) == ('abc',)
    assert cons.get((), mkref('first', lambda r: 'abc'), mkref('rest', lambda r: ())) == ('abc',)
    assert cons.get((), mkref('first', lambda r: 'abc'), mkref('rest', lambda r: ('def',))) == ('abc', 'def')
    assert cons.get((), mkref('first', lambda r: 'abc'), mkref('rest', lambda r: ('def', 'ghi'))) == ('abc', 'def', 'ghi')
    assert empty.get(()) == ()
    assert first.get((), mkref('l', lambda r: ('abc', 'def'))) == 'abc'
    assert rest.get((), mkref('l', lambda r: ('abc', 'def', 'ghi'))) == ('def', 'ghi')
    assert rest.get((), mkref('l', lambda r: ('abc',))) == ()
    assert append.get((), mkref('l', lambda r: ('abc', 'def')), mkref('b', lambda r: ('ghi',))) == ('abc', 'def', 'ghi')
    assert itemnb.get((), mkref('i', lambda r: 1), mkref('l', lambda r: ('abc', 'def', 'ghi'))) == 'def'
    assert find.get((), mkref('n', lambda r: "'d"), mkref('l', lambda r: (("'a", 'abc'), ("'d", 'def'), ("'g", 'ghi')))) == 'def'
    assert reverse.get((), mkref('l', lambda r: ('abc', 'def', 'ghi'))) == ('ghi', 'def', 'abc')
    assert range_.get((), mkref('a', lambda r: '1'), mkref('b', lambda r: '4')) == (1, 2, 3)

    assert map_.get((), mkref('item', lambda r: "'i"), mkref('transform', lambda r: valueof.get(r, mkprop('name', lambda: 'i')) * 2), mkref('list', lambda r: (1, 2, 3))) == (2, 4, 6)
    assert filter_.get((), mkref('item', lambda r: "'i"), mkref('cond', lambda r: valueof.get(r, mkprop('name', lambda: 'i')) % 2 == 0), mkref('list', lambda r: (1, 2, 3, 4))) == (2, 4)
    assert reduce_.get((), mkref('item', lambda r: "'i"), mkref('accum', lambda r: "'a"), mkref('transform', lambda r: valueof.get(r, mkprop('name', lambda: 'a')) + valueof.get(r, mkprop('name', lambda: 'i'))), mkref('list', lambda r: (1, 2, 3, 4))) == 10
    return True

def testMath():
    assert add.get((), mkref('a', lambda r: 3), mkref('b', lambda r: 2)) == 5
    assert subtract.get((), mkref('a', lambda r: 3), mkref('b', lambda r: 2)) == 1
    assert multiply.get((), mkref('a', lambda r: 2), mkref('b', lambda r: 3)) == 6
    assert divide.get((), mkref('a', lambda r: 3), mkref('b', lambda r: 2)) == 1.5
    return True

def testURL():
    assert host.get((), mkprop('host', lambda: 'localhost')) == 'localhost'
    assert path.get((), mkprop('path', lambda: ('abc', 'def'))) == ('abc', 'def')
    assert params.get((), mkprop('params', lambda: (("'a", 'abc'), ("'d", 'def')))) == (("'a", 'abc'), ("'d", 'def'))
    assert user.get((), mkprop('user', lambda: 'joe')) == 'joe'
    assert realm.get((), mkprop('realm', lambda: 'localhost')) == 'localhost'
    assert email.get((), mkprop('email', lambda: 'joe@localhost')) == 'joe@localhost'
    return True

def testText():
    assert contains.get((), mkref('sub', lambda r: 'bc'), mkref('s', lambda r: 'abcd')) == True
    assert split.get((), mkref('sep', lambda r: 'x'), mkref('s', lambda r: 'abcxdef')) == ('abc', 'def')
    assert join.get((), mkref('sep', lambda r: 'x'), mkref('s', lambda r: ('abc', 'def'))) == 'abcxdef'
    assert replace.get((), mkref('from', lambda r: 'x'), mkref('to', lambda r: 'y'), mkref('s', lambda r: 'abcxdefxghi')) == 'abcydefyghi'
    assert lowercase.get((), mkref('s', lambda r: 'ABC')) == 'abc'
    assert uppercase.get((), mkref('s', lambda r: 'abc')) == 'ABC'
    return True

if __name__ == '__main__':
    print 'Testing...'
    testValues()
    testLogic()
    testLists()
    testMath()
    testURL()
    testText()
    print 'OK'


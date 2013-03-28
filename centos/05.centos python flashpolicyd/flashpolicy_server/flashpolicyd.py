#!/usr/bin/env python
#
# flashpolicyd.py
# Simple socket policy file server for Flash
#
# Usage: flashpolicyd.py --file=FILE
#
# Logs to stderr
# Requires Python 2.5 or later

from __future__ import with_statement

import sys
import optparse
import socket
try:
    import thread
except ImportError:
    import _thread as thread #python 3.2
#import exceptions
import contextlib
def a_has_b_2(a, b):
    nPos = a.find(b)
    if nPos<0:
        return False
    else:
        return True
VERSION = 0.1
_port = 843
class policy_server(object):
    def __init__(self, port, path):
        self.port = port
        self.path = path
        self.policy = self.read_policy(path)
        self.log('Listening on port %d\n' % port)
        #print(self.policy)
        try:
            self.sock = socket.socket(socket.AF_INET6, socket.SOCK_STREAM)
        except AttributeError:
            # AttributeError catches Python built without IPv6
            self.sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        except socket.error:
            # socket.error catches OS with IPv6 disabled
            self.sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.sock.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
        self.sock.bind(('', port))
        self.sock.listen(5)
    def read_policy(self, path):
        f=open(path,'rb')
        policy = f.read(10001)
        if len(policy) > 10000:
            raise exceptions.RuntimeError('File probably too large to be a policy file',path)
        #if 'cross-domain-policy' not in policy:
        #    raise exceptions.RuntimeError('Not a valid policy file',path)
        return policy
    def run(self):
        try:
            while True:
                thread.start_new_thread(self.handle, self.sock.accept())
        except socket.error:
            self.log('Error accepting connection ')
    def handle(self, conn, addr):
        addrstr = '%s:%s' % (addr[0],addr[1])
        try:
            self.log('Connection from %s' % (addrstr))
            with contextlib.closing(conn):
                # It's possible that we won't get the entire request in
                # a single recv, but very unlikely.
                request = conn.recv(1024).strip()
                requ = bytes.decode(request)
                print(type(requ))
                if not a_has_b_2(requ,"<policy-file-request/>\0"):#requ != 'b<policy-file-request/>\x00':
                    self.log('Unrecognized request from %s: %s' % (addrstr, request))
                    return
                self.log('Valid request received from %s' % (addrstr))
                conn.sendall(self.policy)
                self.log('Sent policy file to %s' % (addrstr))
        except socket.error:
            self.log('Error handling connection from %s: %s' % (addrstr))
        except e:
            self.log('Error handling connection from %s: %s' % (addrstr))
    def log(self, str):
        print(str)

def main():
    parser = optparse.OptionParser(usage = '%prog --file=FILE',
                                   version='%prog ' + str(VERSION))
    parser.add_option('-f', '--file', dest='path',
                      help='server policy file FILE', metavar='FILE')
    opts, args = parser.parse_args()
    if args:
        parser.error('No arguments are needed. See help.')
    if not opts.path:
        parser.error('File must be specified. See help.')

    try:
        policy_server(_port, opts.path).run()
    except KeyboardInterrupt:
        pass

if __name__ == '__main__':
    main()

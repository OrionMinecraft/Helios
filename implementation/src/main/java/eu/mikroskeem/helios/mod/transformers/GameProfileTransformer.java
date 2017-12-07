/*
 * This file is part of project Helios, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2017 Mark Vainomaa <mikroskeem@mikroskeem.eu>
 * Copyright (c) Contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package eu.mikroskeem.helios.mod.transformers;

import eu.mikroskeem.orion.api.bytecode.OrionTransformer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.util.TraceClassVisitor;

import java.io.PrintWriter;
import java.util.UUID;

import static eu.mikroskeem.shuriken.instrumentation.Descriptor.newDescriptor;

/**
 * GameProfile transformer
 *
 * @author Mark Vainomaa
 */
public final class GameProfileTransformer implements OrionTransformer {
    private final static Logger logger = LogManager.getLogger(GameProfileTransformer.class);

    private final static String GAME_PROFILE = "com/mojang/authlib/GameProfile";
    private final static String GAME_PROFILE_PROPERTY = "com/mojang/authlib/properties/Property";
    private final static String PLAYER_PROFILE = "eu/mikroskeem/playerprofile/PlayerProfile";
    private final static String PLAYER_PROFILE_PROPERTY = "eu/mikroskeem/playerprofile/PlayerProfileProperty";
    private final static String DESC_SU = newDescriptor().accepts(String.class, UUID.class).toString();
    private final static String DESC_SS = newDescriptor().accepts(String.class, String.class).toString();
    private final static String DESC_SSS = newDescriptor().accepts(String.class, String.class, String.class).toString();

    @NotNull
    @Override
    public byte[] transformClass(@NotNull byte[] source, @NotNull String className, @NotNull String remappedClassName) {
        String raw = className.replace('.', '/');
        if(!(raw.equals(GAME_PROFILE) || raw.equals(GAME_PROFILE_PROPERTY)))
            return source;

        logger.info("Transforming class {}", className);
        ClassReader cr = new ClassReader(source);
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        cr.accept(new GameProfileVisitor(cw, raw), 0);
        return cw.toByteArray();
    }

    static class GameProfileVisitor extends ClassVisitor implements Opcodes {
        String className;

        GameProfileVisitor(ClassVisitor visitor, String className) {
            super(ASM5, visitor);
            this.className = className;
        }

        @Override
        public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
            if(GAME_PROFILE.equals(name)) {
                superName = PLAYER_PROFILE;
            } else if(GAME_PROFILE_PROPERTY.equals(name)) {
                superName = PLAYER_PROFILE_PROPERTY;
            } else {
                throw new IllegalStateException("Why am I here? " + name);
            }
            super.visit(version, access, name, signature, superName, interfaces);
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            if("<init>".equals(name)) {
                return new GameProfileConstructorVisitor(super.visitMethod(access, name, desc, signature, exceptions), className);
            }
            return super.visitMethod(access, name, desc, signature, exceptions);
        }
    }

    static class GameProfileConstructorVisitor extends MethodVisitor implements Opcodes {
        String className;

        GameProfileConstructorVisitor(MethodVisitor visitor, String className) {
            super(ASM5, visitor);
            this.className = className;
        }

        @Override
        public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
            if(opcode == INVOKESPECIAL && "<init>".equals(name) && "java/lang/Object".equals(owner)) {
                if(GAME_PROFILE.equals(className)) {
                    owner = PLAYER_PROFILE;
                    desc = DESC_SU;

                    // Load parameters to stack as well
                    super.visitVarInsn(ALOAD, 2);
                    super.visitVarInsn(ALOAD, 1);

                } else if(GAME_PROFILE_PROPERTY.equals(className)) {
                    owner = PLAYER_PROFILE_PROPERTY;

                    // Load parameters to stack as well
                    super.visitVarInsn(ALOAD, 1);
                    super.visitVarInsn(ALOAD, 2);

                    if(DESC_SSS.equals(desc)) {
                        super.visitVarInsn(ALOAD, 3);
                    } else if(!DESC_SS.equals(desc)) {
                        throw new IllegalStateException("Unknown Property desc: " + desc);
                    }
                } else {
                    throw new IllegalStateException("Why am I here? " + className);
                }
            }
            super.visitMethodInsn(opcode, owner, name, desc, itf);
        }
    }
}

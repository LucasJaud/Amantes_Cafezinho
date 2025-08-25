let allRoles = [];

// Carregar roles disponíveis
async function loadRoles() {
    try {
        const response = await fetch('/admin/users/roles');
        if (response.ok) {
            allRoles = await response.json();
        }
    } catch (error) {
        console.error('Erro ao carregar roles:', error);
        showToast('Erro ao carregar permissões', 'error');
    }
}

// Abrir modal de edição de roles
// async function openRoleModal(userId) {
//     try {
//         const response = await fetch(`/admin/users/${userId}`);
//         if (response.ok) {
//             const user = await response.json();
//
//             document.getElementById('modalUserId').value = userId;
//             document.getElementById('modalUserInfo').innerHTML = `
//                         <div class="flex items-center">
//                             <div class="h-8 w-8 rounded-full bg-gradient-to-r from-coffee-medium to-coffee-accent flex items-center justify-center text-white font-bold text-sm mr-3">
//                                 ${user.username.charAt(0).toUpperCase()}
//                             </div>
//                             <div>
//                                 <p class="font-medium">${user.username}</p>
//                                 <p class="text-xs text-coffee-dark/60">${user.email}</p>
//                             </div>
//                         </div>
//                     `;
//
//             const rolesContainer = document.getElementById('rolesContainer');
//             rolesContainer.innerHTML = '';
//
//             if (allRoles.length === 0) {
//                 await loadRoles();
//             }
//
//             allRoles.forEach(role => {
//                 const hasRole = user.role && user.role.some(userRole => userRole.id === role.id);
//
//                 const roleColor = {
//                     'ADMIN': 'border-red-300 bg-red-50',
//                     'MANAGER': 'border-blue-300 bg-blue-50',
//                     'MODERATOR': 'border-yellow-300 bg-yellow-50',
//                     'USER': 'border-green-300 bg-green-50'
//                 }[role.name] || 'border-gray-300 bg-gray-50';
//
//                 const roleIcon = {
//                     'ADMIN': 'fas fa-crown',
//                     'MANAGER': 'fas fa-briefcase',
//                     'MODERATOR': 'fas fa-shield',
//                     'USER': 'fas fa-user'
//                 }[role.name] || 'fas fa-user';
//
//                 const div = document.createElement('div');
//                 div.className = `flex items-center p-3 rounded-lg border-2 transition-all hover:shadow-md ${roleColor}`;
//                 div.innerHTML = `
//                             <input type="checkbox" id="role_${role.id}" name="roleIds" value="${role.id}"
//                                    ${hasRole ? 'checked' : ''}
//                                    class="h-4 w-4 text-coffee-medium focus:ring-coffee-medium border-coffee-medium/30 rounded">
//                             <label for="role_${role.id}" class="ml-3 flex items-center cursor-pointer flex-1">
//                                 <i class="${roleIcon} text-coffee-dark mr-2"></i>
//                                 <div>
//                                     <div class="font-medium text-coffee-dark">${role.displayName}</div>
//                                     ${role.description ? `<div class="text-xs text-coffee-dark/60">${role.description}</div>` : ''}
//                                 </div>
//                             </label>
//                         `;
//                 rolesContainer.appendChild(div);
//             });
//
//             document.getElementById('roleModal').classList.remove('hidden');
//         } else {
//             showToast('Erro ao carregar informações do usuário', 'error');
//         }
//     } catch (error) {
//         console.error('Erro ao carregar usuário:', error);
//         showToast('Erro ao carregar informações do usuário', 'error');
//     }
// }

async function openRoleModal(userId) {
    try {
        const response = await fetch(`/admin/users/${userId}`);
        if (response.ok) {
            const user = await response.json();

            document.getElementById('modalUserId').value = userId;
            document.getElementById('modalUserInfo').innerHTML = `
                <div class="flex items-center">
                    <div class="h-8 w-8 rounded-full bg-gradient-to-r from-coffee-medium to-coffee-accent flex items-center justify-center text-white font-bold text-sm mr-3">
                        ${user.username.charAt(0).toUpperCase()}
                    </div>
                    <div>
                        <p class="font-medium">${user.username}</p>
                        <p class="text-xs text-coffee-dark/60">${user.email}</p>
                    </div>
                </div>
            `;

            const rolesContainer = document.getElementById('rolesContainer');
            rolesContainer.innerHTML = '';

            if (!allRoles || allRoles.length === 0) {
                await loadRoles();
            }

            allRoles.forEach(role => {
                // Verifica se o usuário já possui a role
                const hasRole = user.roles && user.roles.some(r => r.name === role.name);

                // Define cores e ícones
                const roleColor = {
                    'ADMIN': 'border-red-300 bg-red-50',
                    'REVIEWER': 'border-blue-300 bg-blue-50',
                    'CAFETERIA': 'border-yellow-300 bg-yellow-50'
                }[role.name] || 'border-gray-300 bg-gray-50';

                const roleIcon = {
                    'ADMIN': 'fas fa-crown',
                    'REVIEWER': 'fas fa-star',
                    'CAFETERIA': 'fas fa-coffee'
                }[role.name] || 'fas fa-user';

                const div = document.createElement('div');
                div.className = `flex items-center p-3 rounded-lg border-2 transition-all hover:shadow-md ${roleColor}`;
                div.innerHTML = `
                    <input type="checkbox" id="role_${role.name}" name="roleNames" value="${role.name}" 
                           ${hasRole ? 'checked' : ''} 
                           class="h-4 w-4 text-coffee-medium focus:ring-coffee-medium border-coffee-medium/30 rounded">
                    <label for="role_${role.name}" class="ml-3 flex items-center cursor-pointer flex-1">
                        <i class="${roleIcon} text-coffee-dark mr-2"></i>
                        <div class="font-medium text-coffee-dark">${role.name}</div>
                    </label>
                `;
                rolesContainer.appendChild(div);
            });

            document.getElementById('roleModal').classList.remove('hidden');
        } else {
            showToast('Erro ao carregar informações do usuário', 'error');
        }
    } catch (error) {
        console.error('Erro ao carregar usuário:', error);
        showToast('Erro ao carregar informações do usuário', 'error');
    }
}

// Fechar modal de roles
function closeRoleModal() {
    document.getElementById('roleModal').classList.add('hidden');
}

// Atualizar roles do usuário
async function updateUserRoles(event) {
    event.preventDefault();

    const userId = document.getElementById('modalUserId').value;
    const checkboxes = document.querySelectorAll('input[name="roleIds"]:checked');
    const roleIds = Array.from(checkboxes).map(cb => parseInt(cb.value));

    try {
        const response = await fetch(`/admin/users/${userId}/roles`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'X-Requested-With': 'XMLHttpRequest'
            },
            body: JSON.stringify({ userId: parseInt(userId), roleIds })
        });

        if (response.ok) {
            showToast('Permissões atualizadas com sucesso!', 'success');
            closeRoleModal();
            setTimeout(() => window.location.reload(), 1500);
        } else {
            const errorText = await response.text();
            showToast(errorText || 'Erro ao atualizar permissões', 'error');
        }
    } catch (error) {
        console.error('Erro ao atualizar roles:', error);
        showToast('Erro ao atualizar permissões', 'error');
    }
}

// Toggle status do usuário
async function toggleUserStatus(userId) {
    if (!confirm('Tem certeza que deseja alterar o status deste usuário?')) {
        return;
    }

    try {
        const response = await fetch(`/admin/users/${userId}/toggle-status`, {
            method: 'PUT',
            headers: {
                'X-Requested-With': 'XMLHttpRequest'
            }
        });

        if (response.ok) {
            showToast('Status do usuário alterado com sucesso!', 'success');
            setTimeout(() => window.location.reload(), 1500);
        } else {
            const errorText = await response.text();
            showToast(errorText || 'Erro ao alterar status', 'error');
        }
    } catch (error) {
        console.error('Erro ao alterar status:', error);
        showToast('Erro ao alterar status do usuário', 'error');
    }
}

// Exibir toast de notificação
function showToast(message, type = 'info') {
    const toast = document.getElementById('toast');
    const toastIcon = document.getElementById('toastIcon');
    const toastMessage = document.getElementById('toastMessage');

    toastMessage.textContent = message;

    // Definir ícone e cor baseado no tipo
    const configs = {
        'success': {
            icon: '<i class="fas fa-check-circle text-green-500 text-lg"></i>',
            bgColor: 'border-l-4 border-green-500'
        },
        'error': {
            icon: '<i class="fas fa-exclamation-triangle text-red-500 text-lg"></i>',
            bgColor: 'border-l-4 border-red-500'
        },
        'info': {
            icon: '<i class="fas fa-info-circle text-blue-500 text-lg"></i>',
            bgColor: 'border-l-4 border-blue-500'
        }
    };

    const config = configs[type] || configs['info'];
    toastIcon.innerHTML = config.icon;
    toast.querySelector('.bg-white').className = `bg-white border border-coffee-medium/30 rounded-lg shadow-2xl p-4 max-w-sm ${config.bgColor}`;

    toast.classList.remove('hidden');

    // Auto-hide após 4 segundos
    setTimeout(hideToast, 4000);
}

// Esconder toast
function hideToast() {
    document.getElementById('toast').classList.add('hidden');
}

// Fechar modais com ESC
document.addEventListener('keydown', function(e) {
    if (e.key === 'Escape') {
        closeRoleModal();
        hideToast();
    }
});

// Fechar modal clicando fora
document.getElementById('roleModal').addEventListener('click', function(e) {
    if (e.target === this) {
        closeRoleModal();
    }
});

// Inicialização
document.addEventListener('DOMContentLoaded', function() {
    loadRoles();
});
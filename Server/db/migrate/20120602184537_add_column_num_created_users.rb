class AddColumnNumCreatedUsers < ActiveRecord::Migration
  def up
  	add_column :users, :num_created, :integer, :default => 0
  end

  def down
  end
end
